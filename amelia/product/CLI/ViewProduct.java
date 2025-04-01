package amelia.product.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import amelia.MyConnection;

// Class responsible for displaying all products from the database
public class ViewProduct 
{
    // Method to display all product records
    public static void viewAllProducts() 
    {
        // Connection object used to connect to the database
        Connection connection = null;

        try 
        {
            // Establish database connection
            connection = MyConnection.getConnection();

            // Prepare SQL statement to select all product data
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT productId, name, category, price, stock FROM Product"
            );

            // Execute the query and store results
            ResultSet result = stmt.executeQuery();

            System.out.println("===== All Products =====");

            // Loop through the result set and display each product
            while (result.next()) 
            {
                System.out.println("-----------------------------------");
                System.out.println("ID:       " + result.getInt("productId"));
                System.out.println("Name:     " + result.getString("name"));
                System.out.println("Category: " + result.getString("category"));
                System.out.println("Price:    $" + result.getDouble("price"));
                System.out.println("Stock:    " + result.getInt("stock"));
            }

        } 
        catch (SQLException e) 
        {
            // Handle any SQL-related exceptions
            System.out.println("Error retrieving product data: " + e.getMessage());
        } 
        finally 
        {
            // Always attempt to close the connection if it was opened
            try 
            {
                if (connection != null) connection.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
