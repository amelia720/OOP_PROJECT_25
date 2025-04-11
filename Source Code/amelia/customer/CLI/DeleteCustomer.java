package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.customer.CustomerDAO;
import amelia.MyConnection;

/**
 * This class allows the user to delete a customer using the command line
 * by interacting with the CustomerDAO class.
 */
public class DeleteCustomer 
{
    /**
     * Deletes a customer based on user input.
     * 
     * Steps:
     * - Connects to the database
     * - Shows all customers
     * - Asks the user to enter the customer ID
     * - Asks for confirmation before deleting
     * - Deletes the customer if confirmed
     * - Closes the connection and scanner
     */
    public static void deleteCust() 
    {
        // Scanner to read user input from console
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        // TRY block: This is where we put the main logic that might throw exceptions
        try 
        {
            // Establish database connection
            conn = MyConnection.getConnection();

            // Display all customers to the user via the ViewCustomer class
            ViewCustomer.viewAllCustomers();

            // Prompt the user for the ID of the customer to delete
            System.out.print("\nEnter the ID of the customer to delete: ");
            int customerId = Integer.parseInt(input.nextLine());

            // Confirm the deletion action
            System.out.print("Are you sure you want to delete this customer? (yes/no): ");
            String confirm = input.nextLine().trim().toLowerCase();

            // If user does not confirm with "yes", cancel the operation
            if (!confirm.equals("yes")) 
            {
                System.out.println("Deletion cancelled.");
                return;
            }

            // Attempt to delete the customer using the DAO method
            int deleted = CustomerDAO.deleteCustomer(conn, customerId);

            // Inform the user of the result
            if (deleted > 0) 
            {
                System.out.println("Customer deleted successfully.");
            } 
            else 
            {
                System.out.println("Customer not found or already deleted.");
            }

        } 
        // CATCH block: If any error occurs above, this handles it so the program doesn't crash
        catch (Exception e) 
        {
            // Print any exception that occurs
            System.out.println("Error: " + e.getMessage());
        } 
        // FINALLY block: This always runs, whether an exception was thrown or not
        finally 
        {
            // Close the database connection and scanner to free resources
            try 
            {
                if (conn != null) conn.close();
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }

            // Close the input scanner
            input.close();
        }
    }

    /**
     * Main method to run the delete customer operation.
     */
    public static void main(String[] args) 
    {
        deleteCust(); // Start the deletion process
    }
}
