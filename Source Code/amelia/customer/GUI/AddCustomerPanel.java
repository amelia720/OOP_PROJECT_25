package amelia.customer.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.sql.Connection;

import amelia.MyConnection;
import amelia.customer.CustomerPanel;
import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

/**
 * A panel for adding a new customer using a GUI form.
 * Extends CustomerPanel and adds an "Insert Customer" button.
 */
public class AddCustomerPanel extends CustomerPanel 
{

    /**
     * Constructor for AddCustomerPanel.
     * Sets up the insert button and handles user actions.
     */
    public AddCustomerPanel() 
    {
        // // Initialize components from CustomerPanel
        super(); // inherits the form and sets up the layout

        // Create the "Insert Customer" button
        JButton insertButton = new JButton("Insert Customer");

        // Label for displaying messages (e.g., success or error)
        JLabel messageLabel = new JLabel();

        // Add a click listener to the button
        insertButton.addActionListener(e -> 
        {
            // Ask the user to confirm before inserting
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to add this customer?",
                "Confirm Insertion",
                JOptionPane.YES_NO_OPTION
            );

            // If the user clicks "No", cancel the operation
            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Insertion cancelled by user.");
                return;
            }

            // Declare database connection
            Connection conn = null;

            try 
            {
                // Get customer data from the form and validate it
                Customer customer = getCustomerData();

                // Connect to the database
                conn = MyConnection.getConnection();

                // Clear the form after getting data
                setCustomerData(new Customer());

                if (conn != null) 
                {
                    // Insert customer into database through the insertCustomer method in CustomerDAO
                    CustomerDAO.insertCustomer(conn, customer);
                    // Show success message
                    messageLabel.setText("Customer inserted successfully!");
                } 
                else 
                {
                    // If connection is null, show error message
                    messageLabel.setText("Database connection failed.");
                }
            } 
            // Handle form validation errors
            catch (IllegalArgumentException ex) // catches errors related to invalid user input 
            {
                // Show validation error message
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } 
            // Handle any other unexpected errors
            catch (Exception ex) 
            {
                // Show error message
                messageLabel.setText("Error: " + ex.getMessage());
            } 
            // Always close the connection, even if there was an error
            // This block always runs — even if an error happened earlier in the try block
            finally 
            {
                // We use another 'try' block here because even closing a connection can throw an error.
                try 
                {
                    // Check if the database connection was actually created
                    if (conn != null) 
                    {
                        // If it exists, try to close it to free up resources
                        conn.close();
                    }
                } 
                // If closing the connection fails, this catch block will handle the error
                catch (Exception e1) 
                {
                    // If something goes wrong while closing the connection, print the error
                    // (for debugging, doesn't crash the program)
                    e1.printStackTrace();
                }
            }

        });

        // Add the button and message label to the panel
        add(insertButton);
        add(messageLabel);
    }
}
