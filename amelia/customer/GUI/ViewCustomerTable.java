package amelia.customer.GUI;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;

import amelia.customer.CustomerDAO;
import amelia.MyConnection;

/**
 * Utility class for handling the customer table in GUI.
 * It provides table headers and loads all customer data into the table.
 */
public class ViewCustomerTable 
{
    /**
     * Returns an array of column headers for the customer table.
     * 
     * @return column titles as a String array
     */
    public static String[] getHeaders() 
    {
        return new String[]{"ID", "First Name", "Surname", "Address", "Email", "Phone"};
    }

    /**
     * Loads all customer records into the given table model.
     * Clears the table first, then adds rows for each customer in the database.
     * 
     * @param model the DefaultTableModel to populate with customer data
     */
    public static void loadAll(DefaultTableModel model) 
    {
        model.setRowCount(0); // Clear any existing rows in the table

        // Try-with-resources: auto-closes the connection
        try (Connection conn = MyConnection.getConnection()) 
        {
            // Get all customers from the database
            ResultSet rs = CustomerDAO.getAllCustomers(conn);

            // Go through each row in the result set
            while (rs.next()) 
            {
                // Create a row with customer info
                Object[] row = 
                {
                    rs.getInt("customerId"),
                    rs.getString("fname"),
                    rs.getString("sname"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getString("phone")
                };

                // Add the row to the table model
                model.addRow(row); //method from DefaultTableModel
            }
        } 
        catch (Exception e) 
        {
            // Print error (could be replaced with proper logging)
            e.printStackTrace(); 
        }
    }
}
