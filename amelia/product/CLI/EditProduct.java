package amelia.product.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.MyConnection;

/**
 * Class that allows the user to update an existing product's details
 * through the command line.
 */
public class EditProduct 
{
    /**
     * Edits a product by taking input from the user and updating it in the database.
     */
    public static void editProd() 
    {
        Connection connection = null;
        Scanner input = new Scanner(System.in); // Used to take user input

        try 
        {
            // Connect to the database
            connection = MyConnection.getConnection();

            // Display all products first so user knows what to choose
            ViewProduct.viewAllProducts();

            // Ask the user for the ID of the product to edit
            System.out.print("\nEnter the ID of the product to edit: ");
            int productId = Integer.parseInt(input.nextLine());

            // Check if a product with that ID exists
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT * FROM Product WHERE productId = ?"
            );
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) 
            {
                // If product not found, show message and exit
                System.out.println("Product not found.");
                return;
            }

            // Create a Product object to hold updated values
            Product product = new Product();
            product.setProductId(productId);

            // Ask user for new product name
            System.out.print("Enter new product name: ");
            product.setName(input.nextLine());

            // Ask user for new category
            System.out.print("Enter new category: ");
            product.setCategory(input.nextLine());

            // Get new price input (validate it's a number and valid)
            while (true) 
            {
                try 
                {
                    System.out.print("Enter new price: ");
                    product.setPrice(Double.parseDouble(input.nextLine()));
                    break;
                } 
                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid number. Please enter a valid price.");
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Get new stock quantity input (validate it's a number and valid)
            while (true) 
            {
                try 
                {
                    System.out.print("Enter new stock quantity: ");
                    product.setStock(Integer.parseInt(input.nextLine()));
                    break;
                } 
                catch (NumberFormatException e) 
                {
                    System.out.println("Invalid number. Please enter a valid stock quantity.");
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Try to update the product in the database
            int rows = ProductDAO.updateProduct(connection, product);
            if (rows > 0) 
            {
                System.out.println("Product updated successfully.");
            } 
            else 
            {
                System.out.println("Update failed.");
            }

        } 
        // Catch and show SQL errors
        catch (SQLException e) 
        {
            System.out.println("SQL Error: " + e.getMessage());
        } 
        // Catch invalid number format in ID input
        catch (NumberFormatException e) 
        {
            System.out.println("Invalid input. Please enter a numeric product ID.");
        } 
        // This block always runs, even if an error occurred
        finally 
        {
            // Try to close the database connection
            try 
            {
                if (connection != null) connection.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Error closing connection: " + e.getMessage());
            }

            // Close the scanner to free up resources
            input.close();
        }
    }
}
