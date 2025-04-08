package amelia.product.CLI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.product.ProductDAO;
import amelia.MyConnection;

/**
 * This class handles deleting a product from the database using CLI input.
 */
public class DeleteProduct 
{

    /**
     * Prompts the user to select a product to delete and confirms the action.
     */
    public static void deleteProd() 
    {
        Scanner input = new Scanner(System.in);  // For reading user input
        Connection conn = null;  // Database connection

        try 
        {
            // Connect to the database
            conn = MyConnection.getConnection();

            // Show all products before asking for deletion
            ViewProduct.viewAllProducts();

            // Ask user for product ID to delete
            System.out.print("\nEnter the ID of the product to delete: ");
            int productId = Integer.parseInt(input.nextLine());

            // Confirm user wants to delete
            System.out.print("Are you sure you want to delete this product? (yes/no): ");
            String confirm = input.nextLine().trim().toLowerCase();

            if (!confirm.equals("yes")) 
            {
                // User cancelled the deletion
                System.out.println("Deletion cancelled.");
                return;
            }

            // Attempt to delete the product via DAO method
            int deleted = ProductDAO.deleteProduct(conn, productId);

            // Show message based on result
            if (deleted > 0) 
            {
                System.out.println("Product deleted successfully.");
            } 
            else 
            {
                System.out.println("Product not found or already deleted.");
            }

        } 
        catch (Exception e) 
        {
            // Catch and show any unexpected errors
            System.out.println("Error: " + e.getMessage());

        } 
        finally 
        {
            // Close the database connection and scanner
            try 
            {
                if (conn != null) conn.close();
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }

            input.close();
        }
    }
}
