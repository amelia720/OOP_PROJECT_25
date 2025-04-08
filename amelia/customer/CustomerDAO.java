package amelia.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Customer DAO ( Data Access Object ):
 * > handles database operations for Customer objects.
 * > It includes methods to insert, update, delete, and retrieve customer data from it's table.
 */
public class CustomerDAO 
{

    /**
     * Inserts a new customer into the Customer table.
     *
     * @param conn The database connection to use.
     * @param customer The customer object containing the data to insert.
     * @throws SQLException If there's a problem with the SQL operation.
     */
    public static void insertCustomer(Connection conn, Customer customer) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "INSERT INTO Customer (fname, sname, address, email, phone) VALUES (?, ?, ?, ?, ?)"
        );
        pstat.setString(1, customer.getFname());
        pstat.setString(2, customer.getSname());
        pstat.setString(3, customer.getAddress());
        pstat.setString(4, customer.getEmail());
        pstat.setString(5, customer.getPhone());
        pstat.executeUpdate();
    }

    /**
     * Updates an existing customer's details in the Customer table.
     *
     * @param conn The database connection to use.
     * @param customer The customer object with the updated data (including customerId).
     * @return The number of rows that were updated.
     * @throws SQLException If there's a problem with the SQL operation.
     */
    public static int updateCustomer(Connection conn, Customer customer) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "UPDATE Customer SET fname=?, sname=?, address=?, email=?, phone=? WHERE customerId=?"
        );
        pstat.setString(1, customer.getFname());
        pstat.setString(2, customer.getSname());
        pstat.setString(3, customer.getAddress());
        pstat.setString(4, customer.getEmail());
        pstat.setString(5, customer.getPhone());
        pstat.setInt(6, customer.getCustomerId());
        return pstat.executeUpdate();
    }

    /**
     * Deletes a customer from the Customer table based on their customer ID.
     *
     * @param conn The database connection to use.
     * @param customerId The ID of the customer to delete.
     * @return The number of rows that were deleted.
     * @throws SQLException If there's a problem with the SQL operation.
     */
    public static int deleteCustomer(Connection conn, int customerId) throws SQLException 
    {
        PreparedStatement pstat = conn.prepareStatement(
            "DELETE FROM Customer WHERE customerId = ?"
        );
        pstat.setInt(1, customerId);
        return pstat.executeUpdate();
    }

    /**
     * Retrieves all customers from the Customer table.
     *
     * @param conn The database connection to use.
     * @return A ResultSet containing all customer records.
     * @throws SQLException If there's a problem with the SQL operation.
     */
    public static ResultSet getAllCustomers(Connection conn) throws SQLException 
    {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT customerId, fname, sname, address, email, phone FROM Customer"
        );
        return stmt.executeQuery();
    }

}
