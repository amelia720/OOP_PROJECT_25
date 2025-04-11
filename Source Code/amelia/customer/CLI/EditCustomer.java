package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import amelia.customer.Customer;
import amelia.customer.CustomerDAO;
import amelia.MyConnection;

/**
 * This class lets the user edit an existing customer's details
 * through the command line.
 */
public class EditCustomer 
{
    /**
     * Allows the user to update a customer's information.
     * 
     * <p>This method does the following:
     * <ul>
     *   <li>Connects to the database</li>
     *   <li>Shows a list of all customers</li>
     *   <li>Prompts the user to select a customer by ID</li>
     *   <li>Checks if the customer exists</li>
     *   <li>Asks for new customer details (first name, surname, etc.)</li>
     *   <li>Validates the email and phone number inputs</li>
     *   <li>Updates the customer's information in the database</li>
     * </ul>
     * </p>
     */
    public static void editCust() 
    {
        // Create a Scanner to get input from the user
    Scanner input = new Scanner(System.in);

    // This will hold our database connection
    Connection connection = null;

    // Try block: We put all the code here that might cause an error
    try 
    {
        // Connect to the database
        connection = MyConnection.getConnection();

        // Show all the customers so the user can see them
        ViewCustomer.viewAllCustomers();

        // Ask the user for the ID of the customer they want to edit
        System.out.print("\nEnter the ID of the customer to edit: ");
        int customerId = Integer.parseInt(input.nextLine()); // This can throw an error if input is not a number

        // Check if the customer with that ID exists
        PreparedStatement checkStmt = connection.prepareStatement(
            "SELECT * FROM Customer WHERE customerId = ?"
        );
        checkStmt.setInt(1, customerId);
        ResultSet rs = checkStmt.executeQuery();

        // If no customer found, show message and stop
        if (!rs.next()) 
        {
            System.out.println("Customer not found.");
            return;
        }

        // Create a new customer object to store updated info
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        // Ask for updated customer information
        System.out.print("Enter new first name: ");
        customer.setFname(input.nextLine());

        System.out.print("Enter new surname: ");
        customer.setSname(input.nextLine());

        System.out.print("Enter new address: ");
        customer.setAddress(input.nextLine());

        // Ask for a new email. If it’s not valid, ask again
        while (true) 
        {
            try 
            {
                System.out.print("Enter new email: ");
                customer.setEmail(input.nextLine()); // This can throw an error if email is invalid
                break; // exit the loop if email is valid
            } 
            catch (IllegalArgumentException e) 
            {
                System.out.println("Invalid email: " + e.getMessage());
            }
        }

        // Ask for a new phone number. If it’s not valid, ask again
        while (true) 
        {
            try 
            {
                System.out.print("Enter new phone (10 digits): ");
                customer.setPhone(input.nextLine()); // This can throw an error if phone is invalid
                break; // exit the loop if phone is valid
            } 
            catch (IllegalArgumentException e) 
            {
                System.out.println("Invalid phone: " + e.getMessage());
            }
        }

        // Update the customer in the database
        int rows = CustomerDAO.updateCustomer(connection, customer);

        // If at least one row was updated, it worked
        if (rows > 0) 
        {
            System.out.println("Customer updated successfully.");
        } 
        else 
        {
            System.out.println("Update failed.");
        }

        } 
        // If something goes wrong with the database
        catch (SQLException e) 
        {
            System.out.println("Database error: " + e.getMessage());
        } 
        // If the user enters something that isn't a number
        catch (NumberFormatException e) 
        {
            System.out.println("Invalid input. Please enter a number for the customer ID.");
        } 
        // This block always runs, no matter what
        finally 
        {
            // Close the database connection
            try 
            {
                if (connection != null) connection.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Error closing database: " + e.getMessage());
            }

            // Close the scanner to free up resources
            input.close();
        }
    }

    /**
     * Main method to run the edit customer feature.
     */
    public static void main(String[] args) 
    {
        editCust();
    }
}
