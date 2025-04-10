package amelia.product.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import amelia.MyConnection;

/**
 * A command-line utility class for viewing product records from the database.
 * <p>
 * This class is part of the CLI (Command Line Interface) package and is used
 * to display all products in a formatted list on the console.
 * </p>
 */
public class ViewProduct {

    /**
     * Connects to the database and prints all products to the console.
     * <p>
     * Displays product ID, name, category, price, and stock for each record.
     * Handles exceptions and ensures the connection is properly closed.
     * </p>
     */
    public static void viewAllProducts() {
        Connection connection = null;

        try {
            // Connect to the database
            connection = MyConnection.getConnection();

            // SQL to select all product details
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT productId, name, category, price, stock FROM Product"
            );

            // Execute the query
            ResultSet result = stmt.executeQuery();

            System.out.println("===== All Products =====");

            // Loop through and print each product
            while (result.next()) {
                System.out.println("-----------------------------------");
                System.out.println("ID:       " + result.getInt("productId"));
                System.out.println("Name:     " + result.getString("name"));
                System.out.println("Category: " + result.getString("category"));
                System.out.println("Price:    $" + result.getDouble("price"));
                System.out.println("Stock:    " + result.getInt("stock"));
            }

        } catch (SQLException e) {
            // Handle SQL errors
            System.out.println("Error retrieving product data: " + e.getMessage());
        } finally {
            // Always close the connection
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
