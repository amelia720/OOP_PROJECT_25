package amelia.invoice;

import amelia.customer.Customer;
import amelia.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling invoice-related operations with the database.
 * <p>
 * Includes methods for creating invoices, adding and updating items, adjusting stock,
 * and retrieving or deleting invoices and their related data.
 */
public class InvoiceDAO {

    private Connection conn;

    /**
     * Constructs a new InvoiceDAO with a database connection.
     *
     * @param conn the database connection to be used
     */
    public InvoiceDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Creates a new invoice in the database for the specified customer.
     *
     * @param customerId the ID of the customer
     * @return the generated invoice ID, or -1 if creation failed
     * @throws SQLException if a database error occurs
     */
    public int createInvoice(int customerId) throws SQLException {
        String sql = "INSERT INTO Invoice (customerId) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated invoice ID
                }
            }
        }
        return -1; // Something went wrong
    }

    /**
     * Adds a product to the invoice and updates the stock.
     *
     * @param invoiceId the invoice ID
     * @param productId the product ID
     * @param unitPrice the price per unit
     * @param quantity  the quantity of the product
     * @throws SQLException if there is not enough stock or a database error occurs
     */
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

    /**
     * Updates the stock of a product by subtracting a quantity if enough stock exists.
     *
     * @param productId the product ID
     * @param quantity  the quantity to subtract
     * @return true if the stock was updated, false otherwise
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Updates the quantity of a product in an invoice and adjusts stock accordingly.
     *
     * @param invoiceId the invoice ID
     * @param productId the product ID
     * @param newQty    the new quantity
     * @return true if the quantity was updated, false otherwise
     * @throws SQLException if there is not enough stock or a database error occurs
     */
    public boolean updateInvoiceItemQuantity(int invoiceId, int productId, int newQty) throws SQLException {
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

        if (difference != 0) {
            String updateStockSQL = "UPDATE Product SET stock = stock - ? WHERE productId = ? AND stock >= ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSQL)) {
                stockStmt.setInt(1, difference);
                stockStmt.setInt(2, productId);
                stockStmt.setInt(3, difference > 0 ? difference : 0);
                if (stockStmt.executeUpdate() == 0) {
                    throw new SQLException("Not enough stock to apply the new quantity.");
                }
            }
        }

        String updateQtySQL = "UPDATE InvoiceItem SET quantity = ? WHERE invoiceId = ? AND productId = ?";
        try (PreparedStatement qtyStmt = conn.prepareStatement(updateQtySQL)) {
            qtyStmt.setInt(1, newQty);
            qtyStmt.setInt(2, invoiceId);
            qtyStmt.setInt(3, productId);
            return qtyStmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates the date of a specific invoice.
     *
     * @param conn      the database connection
     * @param invoiceId the invoice ID
     * @param newDate   the new date in "yyyy-MM-dd HH:mm:ss" format
     * @return the number of rows updated
     * @throws SQLException if a database error occurs
     */
    public static int updateInvoiceDate(Connection conn, int invoiceId, String newDate) throws SQLException {
        String sql = "UPDATE Invoice SET invoiceDate = ? WHERE invoiceId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDate);
            stmt.setInt(2, invoiceId);
            return stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a full invoice by ID, including customer info and all items.
     *
     * @param invoiceId the invoice ID
     * @return an {@link Invoice} object with full details, or null if not found
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Retrieves all invoices with basic details, used for table views.
     *
     * @return a list of all invoices
     * @throws SQLException if a database error occurs
     */
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
                invoice.setItems(new ArrayList<>());
                invoice.setTotalAmount(rs.getDouble("total"));

                invoices.add(invoice);
            }
        }

        return invoices;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return a list of all {@link Customer} records
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all {@link Product} records
     * @throws SQLException if a database error occurs
     */
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

    /**
     * Deletes an invoice item and restores the product stock.
     *
     * @param invoiceId the invoice ID
     * @param productId the product ID
     * @return true if the item was deleted, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteInvoiceItem(int invoiceId, int productId) throws SQLException {
        String getQtySql = "SELECT quantity FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
        String deleteSql = "DELETE FROM InvoiceItem WHERE invoiceId = ? AND productId = ?";
        String restoreStockSql = "UPDATE Product SET stock = stock + ? WHERE productId = ?";

        try (
            PreparedStatement getStmt = conn.prepareStatement(getQtySql);
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            PreparedStatement restoreStmt = conn.prepareStatement(restoreStockSql)
        ) {
            getStmt.setInt(1, invoiceId);
            getStmt.setInt(2, productId);
            ResultSet rs = getStmt.executeQuery();
            if (!rs.next()) return false;
            int quantity = rs.getInt("quantity");

            deleteStmt.setInt(1, invoiceId);
            deleteStmt.setInt(2, productId);
            int deleted = deleteStmt.executeUpdate();
            if (deleted == 0) return false;

            restoreStmt.setInt(1, quantity);
            restoreStmt.setInt(2, productId);
            restoreStmt.executeUpdate();

            return true;
        }
    }

    /**
     * Deletes an entire invoice and all its associated items.
     *
     * @param invoiceId the ID of the invoice to delete
     * @return true if the invoice was deleted, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteInvoiceWithItems(int invoiceId) throws SQLException {
        String deleteItemsSql = "DELETE FROM InvoiceItem WHERE invoiceId = ?";
        String deleteInvoiceSql = "DELETE FROM Invoice WHERE invoiceId = ?";

        try (
            PreparedStatement deleteItemsStmt = conn.prepareStatement(deleteItemsSql);
            PreparedStatement deleteInvoiceStmt = conn.prepareStatement(deleteInvoiceSql)
        ) {
            deleteItemsStmt.setInt(1, invoiceId);
            deleteItemsStmt.executeUpdate();

            deleteInvoiceStmt.setInt(1, invoiceId);
            int rows = deleteInvoiceStmt.executeUpdate();
            return rows > 0;
        }
    }
}
