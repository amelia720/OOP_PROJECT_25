package amelia.invoice.GUI;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import amelia.MyConnection;

public class ViewInvoiceTable {

    public static String[] getHeaders() {
        return new String[]{"Invoice ID", "Customer", "Date", "Total"};
    }

    public static void loadAll(DefaultTableModel model) {
        model.setRowCount(0); // Clear table

        String sql = """
            SELECT i.invoiceId, i.invoiceDate,
            CONCAT(c.fname, ' ', c.sname) AS customerName,
            SUM(ii.unitPrice * ii.quantity) AS total
            FROM Invoice i
            INNER JOIN Customer c ON i.customerId = c.customerId
            INNER JOIN InvoiceItem ii ON i.invoiceId = ii.invoiceId
            GROUP BY i.invoiceId
            ORDER BY i.invoiceDate DESC
        """;

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("invoiceId"),
                    rs.getString("customerName"),
                    rs.getTimestamp("invoiceDate"),
                    String.format("€%.2f", rs.getDouble("total"))
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Or route to a logger
        }
    }
}
