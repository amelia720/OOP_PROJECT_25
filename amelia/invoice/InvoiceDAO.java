package amelia.invoice;

import amelia.customer.Customer;
import amelia.product.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private Connection conn;

    public InvoiceDAO(Connection conn) {
        this.conn = conn;
    }

    // 1. Create invoice (returns generated invoiceId)
    public int createInvoice(int customerId) throws SQLException {
        String sql = "INSERT INTO Invoice (customerId) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // 2. Add a product to the invoice with stock check
    public void addInvoiceItem(int invoiceId, int productId, double unitPrice, int quantity) throws SQLException {
        if (!updateStock(productId, quantity)) {
            throw new SQLException("Not enough stock for product ID " + productId);
        }

        String sql = "INSERT INTO InvoiceItem (invoiceId, productId, unitPrice, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            stmt.setInt(2, productId);
            stmt.setDouble(3, unitPrice);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
        }
    }

    // 3. Update stock during insert (if enough)
    private boolean updateStock(int productId, int quantity) throws SQLException {
        String sql = "UPDATE Product SET stock = stock - ? WHERE productId = ? AND stock >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // 4. Update quantity of invoice item + adjust stock accordingly
    public boolean updateInvoiceItemQuantity(int invoiceId, int productId, int newQty) throws SQLException {
        // Step 1: Get current quantity
        String getQtySQL = "SELECT quantity FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
        int oldQty;

        try (PreparedStatement getStmt = conn.prepareStatement(getQtySQL)) {
            getStmt.setInt(1, invoiceId);
            getStmt.setInt(2, productId);
            ResultSet rs = getStmt.executeQuery();
            if (rs.next()) {
                oldQty = rs.getInt("quantity");
            } else {
                throw new SQLException("Invoice item not found.");
            }
        }

        int difference = newQty - oldQty;

        // Step 2: Adjust stock
        if (difference != 0) {
            String updateStockSQL = "UPDATE Product SET stock = stock - ? WHERE productId = ? AND stock >= ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                stockStmt.setInt(1, difference);
                stockStmt.setInt(2, productId);
                stockStmt.setInt(3, difference > 0 ? difference : 0); // ensure only subtracts if needed
                int affected = stockStmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Not enough stock to apply the new quantity.");
                }
            }
        }

        // Step 3: Update quantity
        String updateQtySQL = "UPDATE InvoiceItem SET quantity = ? WHERE invoiceId = ? AND productId = ?";
        try (PreparedStatement qtyStmt = conn.prepareStatement(updateQtySQL)) {
            qtyStmt.setInt(1, newQty);
            qtyStmt.setInt(2, invoiceId);
            qtyStmt.setInt(3, productId);
            int updated = qtyStmt.executeUpdate();
            return updated > 0;
        }
    }

    // 5. Update invoice date
    public static int updateInvoiceDate(Connection conn, int invoiceId, String newDate) throws SQLException {
        String sql = "UPDATE Invoice SET invoiceDate = ? WHERE invoiceId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDate); // format: yyyy-MM-dd HH:mm:ss
            stmt.setInt(2, invoiceId);
            return stmt.executeUpdate();
        }
    }

    // 6. Get invoice with customer and items
    public Invoice getInvoiceById(int invoiceId) throws SQLException {
        Invoice invoice = null;
        List<InvoiceItem> itemList = new ArrayList<>();
        Customer customer = null;

        String sql = """
            SELECT 
                i.invoiceId,
                i.invoiceDate,
                c.customerId, c.fname, c.sname, c.address, c.email, c.phone,
                p.productId, p.name AS productName, p.category,
                ii.unitPrice, ii.quantity, ii.totalAmount
            FROM Invoice i
            INNER JOIN Customer c ON i.customerId = c.customerId
            INNER JOIN InvoiceItem ii ON i.invoiceId = ii.invoiceId
            INNER JOIN Product p ON ii.productId = p.productId
            WHERE i.invoiceId = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceId(rs.getInt("invoiceId"));
                    invoice.setInvoiceDate(rs.getTimestamp("invoiceDate").toLocalDateTime());

                    customer = new Customer();
                    customer.setCustomerId(rs.getInt("customerId"));
                    customer.setFname(rs.getString("fname"));
                    customer.setSname(rs.getString("sname"));
                    customer.setAddress(rs.getString("address"));
                    customer.setEmail(rs.getString("email"));
                    customer.setPhone(rs.getString("phone"));

                    invoice.setCustomer(customer);
                }

                InvoiceItem item = new InvoiceItem();
                item.setProductId(rs.getInt("productId"));
                item.setProductName(rs.getString("productName"));
                item.setCategory(rs.getString("category"));
                item.setUnitPrice(rs.getDouble("unitPrice"));
                item.setQuantity(rs.getInt("quantity"));
                item.setTotalAmount(rs.getDouble("totalAmount"));

                itemList.add(item);
            }
        }

        if (invoice != null) {
            invoice.setItems(itemList);
        }

        return invoice;
    }

    // 7. Get all invoices for table view
    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();

        String sql = """
            SELECT i.invoiceId, i.invoiceDate,
                   c.customerId, c.fname, c.sname, c.address, c.email, c.phone,
                   SUM(ii.unitPrice * ii.quantity) AS total
            FROM Invoice i
            JOIN Customer c ON i.customerId = c.customerId
            JOIN InvoiceItem ii ON i.invoiceId = ii.invoiceId
            GROUP BY i.invoiceId
            ORDER BY i.invoiceDate DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("fname"),
                        rs.getString("sname"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone")
                );

                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getInt("invoiceId"));
                invoice.setInvoiceDate(rs.getTimestamp("invoiceDate").toLocalDateTime());
                invoice.setCustomer(customer);
                invoice.setItems(new ArrayList<>()); // empty list for now
                invoice.setTotalAmount(rs.getDouble("total"));

                invoices.add(invoice);
            }
        }

        return invoices;
    }

    // 8. Get all customers
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("customerId"),
                        rs.getString("fname"),
                        rs.getString("sname"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        }

        return customers;
    }

    // 9. Get all products
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("productId"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }

        return products;
    }
    // public boolean deleteInvoiceItem(int invoiceId, int productId) throws SQLException 
    // {
    //     String sql = "DELETE FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
    //     try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    //         stmt.setInt(1, invoiceId);
    //         stmt.setInt(2, productId);
    //         int deleted = stmt.executeUpdate();
    //         return deleted > 0;
    //     }
    // }

    public boolean deleteInvoiceItem(int invoiceId, int productId) throws SQLException 
    {
        String getQtySql = "SELECT quantity FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
        String deleteSql = "DELETE FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
        String restoreStockSql = "UPDATE Product SET stock = stock + ? WHERE productId = ?";
    
        try (
            PreparedStatement getStmt = conn.prepareStatement(getQtySql);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            PreparedStatement restoreStmt = conn.prepareStatement(restoreStockSql)
        ) {
            // Get quantity first
            getStmt.setInt(1, invoiceId);
            getStmt.setInt(2, productId);
            ResultSet rs = getStmt.executeQuery();
            if (!rs.next()) return false;
            int quantity = rs.getInt("quantity");
    
            // Delete invoice item
            deleteStmt.setInt(1, invoiceId);
            deleteStmt.setInt(2, productId);
            int deleted = deleteStmt.executeUpdate();
            if (deleted == 0) return false;
    
            // Restore stock
            restoreStmt.setInt(1, quantity);
            restoreStmt.setInt(2, productId);
            restoreStmt.executeUpdate();
    
            return true;
        }
    }
    // InvoiceDAO.java
    public boolean deleteInvoiceWithItems(int invoiceId) throws SQLException {
        String deleteItemsSql = "DELETE FROM InvoiceItem WHERE invoiceId = ?";
        String deleteInvoiceSql = "DELETE FROM Invoice WHERE invoiceId = ?";

        try (
            PreparedStatement deleteItemsStmt = conn.prepareStatement(deleteItemsSql);
            PreparedStatement deleteInvoiceStmt = conn.prepareStatement(deleteInvoiceSql)
        ) {
            // Delete associated items
            deleteItemsStmt.setInt(1, invoiceId);
            deleteItemsStmt.executeUpdate();

            // Delete invoice
            deleteInvoiceStmt.setInt(1, invoiceId);
            int rows = deleteInvoiceStmt.executeUpdate();
            return rows > 0;
        }
}

    
}
