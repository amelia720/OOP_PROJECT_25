package amelia.product.GUI;

import javax.swing.*;
import java.sql.Connection;

import amelia.MyConnection;
import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.product.ProductPanel;

public class AddProductPanel extends ProductPanel 
{

    public AddProductPanel() 
    {
        super();

        JButton insertButton = new JButton("Insert Product");
        JLabel messageLabel = new JLabel();

        insertButton.addActionListener(e -> 
        {
            // Confirm insertion
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to add this product?",
                "Confirm Insertion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Insertion cancelled by user.");
                return;
            }

            Connection conn = null;
            try 
            {
                Product product = getProductData(); // Validate input
                conn = MyConnection.getConnection(); // Get DB connection

                if (conn != null) 
                {
                    ProductDAO.insertProduct(conn, product); // Insert product
                    setProductData(new Product()); // Clear form
                    messageLabel.setText("Product inserted successfully!");
                } 
                else 
                {
                    messageLabel.setText("Database connection failed.");
                }

            } 
            catch (NumberFormatException ex) 
            {
                messageLabel.setText("Please enter valid numbers for price and stock.");
            } 
            catch (IllegalArgumentException ex) 
            {
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } 
            catch (Exception ex) 
            {
                messageLabel.setText("Error: " + ex.getMessage());
            } 
            finally 
            {
                try 
                {
                    if (conn != null) conn.close(); // Clean up
                } 
                catch (Exception e1) 
                {
                    e1.printStackTrace();
                }
            }
        });

        add(insertButton);
        add(messageLabel);
    }
}
