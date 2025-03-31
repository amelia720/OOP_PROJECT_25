package amelia.customer.GUI;


import javax.swing.*;
import java.sql.Connection;

import amelia.MyConnection;
import amelia.customer.CustomerPanel;
import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

public class AddCustomerPanel extends CustomerPanel {

    public AddCustomerPanel() {
        super();

        JButton insertButton = new JButton("Insert Customer");
        JLabel messageLabel = new JLabel();

        insertButton.addActionListener(e -> 
        {
            // Confirm insertion
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to add this customer?",
                "Confirm Insertion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Insertion cancelled by user.");
                return;
            }

            Connection conn = null;
            try {
                Customer customer = getCustomerData(); // Validate input
                conn = MyConnection.getConnection();   // Get a fresh connection

                setCustomerData(new Customer()); // Clear form
                if (conn != null) {
                    CustomerDAO.insertCustomer(conn, customer); // Insert
                    messageLabel.setText("Customer inserted successfully!");
                } else {
                    messageLabel.setText("Database connection failed.");
                }
            } catch (IllegalArgumentException ex) {
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            } finally {
                try {
                    if (conn != null) conn.close(); // ✅ Close the connection here
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        add(insertButton);
        add(messageLabel);
    }
}
