package amelia.product.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.sql.Connection;

import amelia.MyConnection;
import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.product.ProductPanel;

/**
 * GUI panel to allow users to add a new product.
 * Inherits form fields from ProductPanel (e.g. name, category, price, stock).
 */
public class AddProductPanel extends ProductPanel 
{
    /**
     * Creates the AddProductPanel with a form and an insert button.
     * When the button is clicked, the product is added to the database.
     */
    public AddProductPanel() 
    {
        super(); // Build the product form layout from the parent ProductPanel class

        // Create a button to insert the product
        JButton insertButton = new JButton("Insert Product");

        // Label to show messages to the user (success or error)
        JLabel messageLabel = new JLabel();

        // Add functionality to the button when it's clicked
        insertButton.addActionListener(e -> 
        {
            // Show a popup asking the user to confirm they want to add the product
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to add this product?",
                "Confirm Insertion",
                JOptionPane.YES_NO_OPTION
            );

            // If the user cancels, show a message and stop
            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Insertion cancelled by user.");
                return;
            }

            Connection conn = null;

            try 
            {
                // Get input from form fields and validate
                Product product = getProductData();

                // Open connection to the database
                conn = MyConnection.getConnection();

                if (conn != null) 
                {
                    // Insert the product into the database
                    ProductDAO.insertProduct(conn, product);

                    // Clear the form after successful insert
                    setProductData(new Product());

                    // Show success message
                    messageLabel.setText("Product inserted successfully!");
                } 
                else 
                {
                    // If connection fails
                    messageLabel.setText("Database connection failed.");
                }

            } 
            // If the user entered letters instead of numbers in price/stock
            catch (NumberFormatException ex) 
            {
                messageLabel.setText("Please enter valid numbers for price and stock.");
            } 
            // If product validation in the setter fails (e.g. negative price)
            catch (IllegalArgumentException ex) 
            {
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } 
            // Catch any other unexpected error
            catch (Exception ex) 
            {
                messageLabel.setText("Error: " + ex.getMessage());
            } 
            finally 
            {
                // Always try to close the database connection
                try 
                {
                    if (conn != null) conn.close();
                } 
                catch (Exception e1) 
                {
                    e1.printStackTrace(); // Print error in console (for debugging)
                }
            }
        });

        // Add the button and message label to the form
        add(insertButton);
        add(messageLabel);
    }
}
