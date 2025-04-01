package amelia.product.GUI;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;

import amelia.MyConnection;
import amelia.product.ProductDAO;

public class ViewProductTable {

    public static String[] getHeaders() {
        return new String[]{"ID", "Name", "Category", "Price", "Stock"};
    }

    public static void loadAll(DefaultTableModel model) {
        model.setRowCount(0);

        try (Connection conn = MyConnection.getConnection()) {
            ResultSet rs = ProductDAO.getAllProducts(conn);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("productId"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadByCategory(DefaultTableModel model, String category) {
        model.setRowCount(0);

        try (Connection conn = MyConnection.getConnection()) {
            String query = "SELECT productId, name, category, price, stock FROM Product WHERE category = ?";
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("productId"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
