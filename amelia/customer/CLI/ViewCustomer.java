package amelia.customer.CLI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import amelia.MyConnection;

// Class responsible for displaying all customers from the database
public class ViewCustomer 
{
    // Method to display all customer records
    public static void viewAllCustomers() 
    {
        // Connection object used to connect to the database
        Connection connection = null;

        try 
        {
            // Establish database connection
            connection = MyConnection.getConnection();

            // Prepare SQL statement to select all customer data
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT customerId, fname, sname, address, email, phone FROM Customer"
            );

            // Execute the query and store results
            ResultSet result = stmt.executeQuery();

            System.out.println("===== All Customers =====");

            // Loop through the result set and display each customer
            while (result.next()) 
            {
                System.out.println("-----------------------------------");
                System.out.println("ID:      " + result.getInt("customerId"));
                System.out.println("Name:    " + result.getString("fname") + " " + result.getString("sname"));
                System.out.println("Address: " + result.getString("address"));
                System.out.println("Email:   " + result.getString("email"));
                System.out.println("Phone:   " + result.getString("phone"));
            }

        } 
        catch (SQLException e) 
        {
            // Handle any SQL-related exceptions
            System.out.println("Error retrieving customer data: " + e.getMessage());
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
