package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.customer.Customer;
import amelia.invoice.InvoiceDAO;
import amelia.invoice.InvoiceItem;
import amelia.invoice.InvoicePanel;
import amelia.product.Product;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

/**
 * A GUI panel that allows the user to create and insert a new invoice.
 */
public class AddInvoicePanel extends JPanel 
{

    /**
     * Constructor that builds the invoice panel UI and sets up logic for inserting invoices.
     */
    public AddInvoicePanel() 
    {
        // Set layout with spacing between components
        setLayout(new BorderLayout(10, 10));

        // Message label for status updates (top of panel)
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Try to connect and load customer/product data
        try (Connection conn = MyConnection.getConnection()) 
        {
            InvoiceDAO dao = new InvoiceDAO(conn);

            // Load all customers and products from the database
            List<Customer> customers = dao.getAllCustomers();
            List<Product> products = dao.getAllProducts();

            // Create the invoice entry form with dropdowns & item table
            InvoicePanel invoicePanel = new InvoicePanel(customers, products);

            // Button for submitting the invoice
            JButton insertButton = new JButton("Insert Invoice");

            // Handle button click
            insertButton.addActionListener(e -> 
            {
                // Ask the user to confirm before inserting
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to insert this invoice?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                // If user cancels, exit the method
                if (confirm != JOptionPane.YES_OPTION) 
                {
                    messageLabel.setText("Insertion cancelled.");
                    return;
                }

                // Connect again to perform the insert
                try (Connection conn2 = MyConnection.getConnection()) 
                {
                    InvoiceDAO invoiceDAO = new InvoiceDAO(conn2);

                    // Get user selections from the form
                    Customer selectedCustomer = invoicePanel.getSelectedCustomer();
                    List<InvoiceItem> items = invoicePanel.getInvoiceItems();

                    // Validate input: must have a customer and at least one item
                    if (selectedCustomer == null || items.isEmpty()) 
                    {
                        messageLabel.setText("Customer and at least one item are required.");
                        return;
                    }

                    // Insert the invoice into the database
                    int invoiceId = invoiceDAO.createInvoice(selectedCustomer.getCustomerId());

                    // Insert each item into the invoice
                    for (InvoiceItem item : items) 
                    {
                        invoiceDAO.addInvoiceItem(
                                invoiceId,
                                item.getProductId(),
                                item.getUnitPrice(),
                                item.getQuantity()
                        );
                    }

                    // Clear form after successful insert
                    invoicePanel.clearForm();
                    messageLabel.setText("Invoice #" + invoiceId + " inserted successfully!");

                } 
                catch (Exception ex) 
                {
                    // Handle any errors during insert
                    ex.printStackTrace();
                    messageLabel.setText("Error: " + ex.getMessage());
                }
            });

            // Add UI components to the panel
            add(invoicePanel, BorderLayout.CENTER);   // Form in the center
            add(insertButton, BorderLayout.SOUTH);    // Button at the bottom
            add(messageLabel, BorderLayout.NORTH);    // Status message at the top

        } 
        catch (Exception ex) 
        {
            // Handle error if data fails to load at the beginning
            ex.printStackTrace();
            add(new JLabel("Failed to load data: " + ex.getMessage()), BorderLayout.CENTER);
        }
    }
}
