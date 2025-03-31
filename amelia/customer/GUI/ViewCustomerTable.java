package amelia.customer.GUI;


import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;

import amelia.customer.CustomerDAO;
import amelia.MyConnection;

public class ViewCustomerTable 
{

    public static String[] getHeaders() 
    {
        return new String[]{"ID", "First Name", "Surname", "Address", "Email", "Phone"};
    }

    public static void loadAll(DefaultTableModel model) 
    {
        model.setRowCount(0); // Clear table

        try (Connection conn = MyConnection.getConnection()) 
        {
            ResultSet rs = CustomerDAO.getAllCustomers(conn);

            while (rs.next()) 
            {
                Object[] row = 
                {
                    rs.getInt("customerId"),
                    rs.getString("fname"),
                    rs.getString("sname"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getString("phone")
                };
                model.addRow(row);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace(); // For now — or route to logger
        }
    }
}
