package amelia.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * ProductDAO handles all database operations related to the Product table.
 */
public class ProductDAO 
{

    /**
     * Inserts a new product into the database.
     * 
     * @param conn    Active DB connection
     * @param product Product object with all data to insert
     */
    public static void insertProduct(Connection conn, Product product) throws SQLException 
    {
        // SQL insert statement with placeholders for values
        PreparedStatement pstat = conn.prepareStatement(
            "INSERT INTO Product (name, category, price, stock) VALUES (?, ?, ?, ?)"
        );

        // Fill in values from the Product object
        pstat.setString(1, product.getName());
        pstat.setString(2, product.getCategory());
        pstat.setDouble(3, product.getPrice());
        pstat.setInt(4, product.getStock());

        // Run the insert query
        pstat.executeUpdate();
    }

    /**
     * Updates an existing product in the database.
     * 
     * @param conn    Active DB connection
     * @param product Product object with updated values
     * @return        Number of rows affected
     */
    public static int updateProduct(Connection conn, Product product) throws SQLException 
    {
        // SQL update query using WHERE clause to match productId
        PreparedStatement pstat = conn.prepareStatement(
            "UPDATE Product SET name=?, category=?, price=?, stock=? WHERE productId=?"
        );

        // Fill in the updated values
        pstat.setString(1, product.getName());
        pstat.setString(2, product.getCategory());
        pstat.setDouble(3, product.getPrice());
        pstat.setInt(4, product.getStock());
        pstat.setInt(5, product.getProductId());

        // Run the update query and return how many rows were changed
        return pstat.executeUpdate();
    }

    /**
     * Deletes a product by its ID.
     * 
     * @param conn      Active DB connection
     * @param productId ID of the product to delete
     * @return          Number of rows deleted
     */
    public static int deleteProduct(Connection conn, int productId) throws SQLException 
    {
        // SQL delete query
        PreparedStatement pstat = conn.prepareStatement(
            "DELETE FROM Product WHERE productId=?"
        );

        // Set the ID of the product to delete
        pstat.setInt(1, productId);

        // Run the delete query
        return pstat.executeUpdate();
    }

    /**
     * Fetches all products from the database.
     * 
     * @param conn Active DB connection
     * @return     ResultSet containing all products
     */
    public static ResultSet getAllProducts(Connection conn) throws SQLException 
    {
        // SQL select query
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT productId, name, category, price, stock FROM Product"
        );

        // Return the result set
        return stmt.executeQuery();
    }
}
