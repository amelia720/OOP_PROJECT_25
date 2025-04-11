package amelia.product.GUI;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;

import amelia.MyConnection;
import amelia.product.ProductDAO;

/**
 * Utility class for loading product data into a table model.
 * <p>
 * Provides methods to get column headers and load products into a JTable,
 * either all products or filtered by category.
 * </p>
 */
public class ViewProductTable 
{

    /**
     * Returns the column headers used in the product table.
     *
     * @return An array of column names: ID, Name, Category, Price, Stock
     */
    public static String[] getHeaders() 
    {
        return new String[]{"ID", "Name", "Category", "Price", "Stock"};
    }

    /**
     * Loads all products from the database into the given table model.
     *
     * @param model The table model where product rows will be added.
     */
    public static void loadAll(DefaultTableModel model) 
    {
        model.setRowCount(0); // Clear existing rows

        try (Connection conn = MyConnection.getConnection()) 
        {
            // Get all products from the database
            ResultSet rs = ProductDAO.getAllProducts(conn);

            // Loop through results and add each product to the table model
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("productId"),      // ID
                    rs.getString("name"),        // Name
                    rs.getString("category"),    // Category
                    rs.getDouble("price"),       // Price
                    rs.getInt("stock")           // Stock
                };
                model.addRow(row);
            }

        } catch (Exception e) 
        {
            // Print error if something goes wrong (like database error)
            e.printStackTrace();
        }
    }

    /**
     * Loads products that belong to a specific category into the table model.
     *
     * @param model    The table model where product rows will be added.
     * @param category The category to filter products by.
     */
    public static void loadByCategory(DefaultTableModel model, String category) 
    {
        model.setRowCount(0); // Clear existing rows

        try (Connection conn = MyConnection.getConnection()) 
        {
            // Prepare SQL query with a placeholder for category
            String query = "SELECT productId, name, category, price, stock FROM Product WHERE category = ?";
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, category); // Set category value
            ResultSet rs = stmt.executeQuery();

            // Loop through filtered products and add them to the table
            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("productId"),      // ID
                    rs.getString("name"),        // Name
                    rs.getString("category"),    // Category
                    rs.getDouble("price"),       // Price
                    rs.getInt("stock")           // Stock
                };
                model.addRow(row);
            }

        } 
        catch (Exception e) 
        {
            // Print error if query fails or connection is bad
            e.printStackTrace();
        }
    }
}
