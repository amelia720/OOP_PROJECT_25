package amelia.product.CLI;

import java.sql.Connection;
import java.util.Scanner;

import amelia.MyConnection;
import amelia.product.Product;
import amelia.product.ProductDAO;

public class InsertProduct 
{

    // Main method to run the insert feature from the command line
    public static void main(String[] args) 
    {
        insertProd(); // CLI entry point
    }

    /**
     * Inserts a new product into the database by asking the user for input.
     */
    public static void insertProd() 
    {
        Scanner input = new Scanner(System.in); // For reading user input
        Connection conn = null; // Will hold the database connection

        try 
        {
            // Create a Product object to hold user input
            Product product = new Product();

            // Ask user for product name and category
            System.out.print("Enter product name: ");
            product.setName(input.nextLine());

            System.out.print("Enter category: ");
            product.setCategory(input.nextLine());

            // Price validation loop
            while (true) 
            {
                try 
                {
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(input.nextLine());
                    product.setPrice(price); // Also validates inside setter
                    break; // Exit loop if no error
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

            // Stock quantity validation loop
            while (true) 
            {
                try {
                    System.out.print("Enter stock quantity: ");
                    int stock = Integer.parseInt(input.nextLine());
                    product.setStock(stock); // Also validates inside setter
                    break; // Exit loop if no error
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

            // Get a database connection and insert product
            conn = MyConnection.getConnection();
            ProductDAO.insertProduct(conn, product); // Save to database

            System.out.println("Product added successfully."); // Success message

        } 
        catch (Exception e) 
        {
            // Handle unexpected errors
            System.out.println("Error: " + e.getMessage());
        } 
        finally 
        {
            // Always close the connection and scanner
            try 
            {
                if (conn != null) conn.close();
            } 
            catch (Exception e) 
            {
                System.out.println("⚠️ Failed to close connection: " + e.getMessage());
            }
            input.close(); // Close scanner
        }
    }
}
