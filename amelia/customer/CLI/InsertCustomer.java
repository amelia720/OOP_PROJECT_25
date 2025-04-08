package amelia.customer.CLI;

import java.sql.Connection;
import java.util.Scanner;
import amelia.MyConnection;

import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

/**
 * This class lets the user add a new customer using the command line.
 */
public class InsertCustomer 
{

    /**
     * Main method that starts the insert customer process.
     */
    public static void main(String[] args) 
    {
        insertCust(); // Start the insert customer process
    }

    /**
     * Collects user input and inserts a new customer into the database.
     */
    public static void insertCust() 
    {
        // Create a Scanner to get input from the user
        Scanner input = new Scanner(System.in);

        // Create a database connection (will be used later)
        Connection conn = null;

        // Try block for main logic
        try 
        {
            // Create a new Customer object to hold the user's input
            Customer customer = new Customer();

            // Ask the user for customer details
            System.out.print("Enter first name: ");
            customer.setFname(input.nextLine());

            System.out.print("Enter surname: ");
            customer.setSname(input.nextLine());

            System.out.print("Enter address: ");
            customer.setAddress(input.nextLine());

            // Loop until a valid email is entered
            while (true) 
            {
                try 
                {
                    System.out.print("Enter email: ");
                    customer.setEmail(input.nextLine()); // May throw IllegalArgumentException
                    break; // Exit loop if valid
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Validation Error: " + e.getMessage());
                }
            }

            // Loop until a valid phone number is entered
            while (true) 
            {
                try 
                {
                    System.out.print("Enter phone (10 digits): ");
                    customer.setPhone(input.nextLine()); // May throw IllegalArgumentException
                    break; // Exit loop if valid
                } 
                catch (IllegalArgumentException e) 
                {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            // Connect to the database
            conn = MyConnection.getConnection();

            // Insert the new customer into the database using the DAO
            CustomerDAO.insertCustomer(conn, customer);

            // Let the user know it worked
            System.out.println("Customer added successfully.");

        } 
        // Catch any unexpected errors
        catch (Exception e) 
        {
            System.out.println("Error: " + e.getMessage());
        } 
        // This block always runs, even if there's an error
        finally 
        {
            try 
            {
                // Close the database connection
                if (conn != null) conn.close();
            } 
            catch (Exception e) 
            {
                System.out.println("⚠️ Failed to close connection: " + e.getMessage());
            }

            // Close the scanner to free up system resources
            input.close();
        }
    }
}
