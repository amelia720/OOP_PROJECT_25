package amelia.invoice.GUI;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import amelia.MyConnection;

/**
 * Utility class for managing invoice data in a table.
 * <p>
 * Provides methods to retrieve table headers and load invoice records
 * from the database into a {@link DefaultTableModel}.
 * </p>
 */
public class ViewInvoiceTable 
{

    /**
     * Returns the column headers for the invoice table.
     *
     * @return an array of column titles: "Invoice ID", "Customer", "Date", "Total"
     */
    public static String[] getHeaders() 
    {
        return new String[]{"Invoice ID", "Customer", "Date", "Total"};
    }

    /**
     * Loads all invoice records into the given table model.
     * <p>
     * It retrieves data by joining the Invoice, Customer, and InvoiceItem tables.
     * Each row contains the invoice ID, customer full name, date, and total amount.
     * </p>
     *
     * @param model the {@link DefaultTableModel} to populate with invoice rows
     */
    public static void loadAll(DefaultTableModel model) 
    {
        model.setRowCount(0); // Clear any existing data in the table

        // SQL query to join invoice, customer, and invoice items
        String sql = """
            SELECT i.invoiceId, i.invoiceDate,
            CONCAT(c.fname, ' ', c.sname) AS customerName,
            SUM(ii.unitPrice * ii.quantity) AS total
            FROM Invoice i
            INNER JOIN Customer c ON i.customerId = c.customerId
            INNER JOIN InvoiceItem ii ON i.invoiceId = ii.invoiceId
            GROUP BY i.invoiceId
            ORDER BY i.invoiceId ASC
        """;

        try (
            Connection conn = MyConnection.getConnection();              // Open DB connection
            PreparedStatement stmt = conn.prepareStatement(sql);        // Prepare the SQL
            ResultSet rs = stmt.executeQuery()                           // Execute query
        ) {
            // Add each row of result into the table model
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("invoiceId"),
                    rs.getString("customerName"),
                    rs.getTimestamp("invoiceDate"),
                    String.format("€%.2f", rs.getDouble("total"))
                };
                model.addRow(row);
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace(); // You could log this in a real application
        }
    }
}
