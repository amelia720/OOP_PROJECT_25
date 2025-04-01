package amelia.product;

import java.sql.*;

public class ProductDAO 
{

    public static void insertProduct(Connection conn, Product product) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "INSERT INTO Product (name, category, price, stock) VALUES (?, ?, ?, ?)"
        );
        pstat.setString(1, product.getName());
        pstat.setString(2, product.getCategory());
        pstat.setDouble(3, product.getPrice());
        pstat.setInt(4, product.getStock());
        pstat.executeUpdate();
    }

    public static int updateProduct(Connection conn, Product product) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "UPDATE Product SET name=?, category=?, price=?, stock=? WHERE productId=?"
        );
        pstat.setString(1, product.getName());
        pstat.setString(2, product.getCategory());
        pstat.setDouble(3, product.getPrice());
        pstat.setInt(4, product.getStock());
        pstat.setInt(5, product.getProductId());
        return pstat.executeUpdate();
    }

    public static int deleteProduct(Connection conn, int productId) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "DELETE FROM Product WHERE productId=?"
        );
        pstat.setInt(1, productId);
        return pstat.executeUpdate();
    }

    public static ResultSet getAllProducts(Connection conn) throws SQLException 
    {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT productId, name, category, price, stock FROM Product"
        );
        return stmt.executeQuery();
    }
}
