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

    // 2. Add a product to the invoice
    public void addInvoiceItem(int invoiceId, int productId, double unitPrice, int quantity) throws SQLException {
        String sql = "INSERT INTO InvoiceItem (invoiceId, productId, unitPrice, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            stmt.setInt(2, productId);
            stmt.setDouble(3, unitPrice);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
        }
    }

    // 3. Get a complete invoice by ID (customer + items)
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

    // 4. Get all customers for dropdown
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

    // 5. Get all products for dropdown
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
}
