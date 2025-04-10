package amelia.product.CLI;

import java.sql.Connection;
import java.util.Scanner;

import amelia.MyConnection;
import amelia.product.Product;
import amelia.product.ProductDAO;

/**
 * A CLI (Command Line Interface) class for inserting a new product into the database.
 * <p>
 * Prompts the user to enter product details, validates the input, and inserts
 * the product record using the {@link ProductDAO}.
 * </p>
 */
public class InsertProduct {

    /**
     * Starts the product insertion process by prompting the user for:
     * - Product name
     * - Category
     * - Price (validated as non-negative number)
     * - Stock quantity (validated as non-negative integer)
     * <p>
     * Then it connects to the database and inserts the product.
     * </p>
     */
    public static void insertProd() {
        Scanner input = new Scanner(System.in); // For reading user input
        Connection conn = null; // Will hold the database connection

        try {
            // Create a Product object to hold user input
            Product product = new Product();

            // Ask user for product name
            System.out.print("Enter product name: ");
            product.setName(input.nextLine());

            // Ask user for category
            System.out.print("Enter category: ");
            product.setCategory(input.nextLine());

            // Price input and validation
            while (true) {
                try {
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(input.nextLine());
                    product.setPrice(price); // Setter does validation
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid price.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Stock quantity input and validation
            while (true) {
                try {
                    System.out.print("Enter stock quantity: ");
                    int stock = Integer.parseInt(input.nextLine());
                    product.setStock(stock); // Setter does validation
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid stock quantity.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Insert product into the database
            conn = MyConnection.getConnection();
            ProductDAO.insertProduct(conn, product);

            System.out.println("Product added successfully.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Cleanup: close DB connection and scanner
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
            input.close();
        }
    }

    /**
     * Entry point for running this class.
     * <p>
     * Starts the product insertion workflow from the CLI.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        insertProd(); // Launch the insertion process
    }
}
