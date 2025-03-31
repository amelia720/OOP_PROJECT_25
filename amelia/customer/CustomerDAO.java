package amelia.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

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

    public static int deleteCustomer(Connection conn, int customerId) throws SQLException {
        PreparedStatement pstat = conn.prepareStatement(
            "DELETE FROM Customer WHERE customerId = ?"
        );
        pstat.setInt(1, customerId);
        return pstat.executeUpdate();
    }
    
    public static ResultSet getAllCustomers(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
            "SELECT customerId, fname, sname, address, email, phone FROM Customer"
        );
        return stmt.executeQuery();
    }
    
}
