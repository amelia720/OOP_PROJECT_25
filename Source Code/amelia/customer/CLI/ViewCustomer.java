package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import amelia.MyConnection;

/**
 * Class responsible for displaying all customers from the database.
 */
public class ViewCustomer 
{
    /**
     * Displays all customer records from the database.
     */
    public static void viewAllCustomers() 
    {
        // Declare connection outside try so it's accessible in finally
        Connection connection = null;

        try 
        {
            // Get connection to the database using MyConnection helper class
            connection = MyConnection.getConnection();

            // Prepare SQL query to fetch all customer details
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT customerId, fname, sname, address, email, phone FROM Customer"
            );

            // Execute the query and store results in a ResultSet
            ResultSet result = stmt.executeQuery();

            // Print header
            System.out.println("===== All Customers =====");

            // Loop through each customer in the result set and print their info
            while (result.next()) 
            {
                System.out.println("-----------------------------------");
                System.out.println("ID:      " + result.getInt("customerId")); // Get ID from column
                System.out.println("Name:    " + result.getString("fname") + " " + result.getString("sname")); // Full name
                System.out.println("Address: " + result.getString("address")); // Address
                System.out.println("Email:   " + result.getString("email")); // Email
                System.out.println("Phone:   " + result.getString("phone")); // Phone
            }

        } 
        catch (SQLException e) 
        {
            // If something goes wrong with the SQL or DB, print the error
            System.out.println("Error retrieving customer data: " + e.getMessage());
        } 
        finally 
        {
            // This block runs no matter what — used to safely close the connection
            try 
            {
                if (connection != null) connection.close(); // Always close the connection to free resources
            } 
            catch (SQLException e) 
            {
                // If closing the connection fails, print error
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
