package amelia.product.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.sql.Connection;

import amelia.MyConnection;
import amelia.product.ProductDAO;

/**
 * A panel that lets the user delete a product from the system.
 * <p>
 * Shows a table of products. User can filter by category,
 * select a product, and delete it after confirmation.
 * </p>
 */
public class DeleteProductPanel extends JPanel 
{

    // Table to display products
    private JTable productTable;

    // Table model to hold product data
    private DefaultTableModel tableModel;

    // Button to trigger deletion
    private JButton deleteButton = new JButton("Delete Product");

    // Label to display messages (status or errors)
    private JLabel messageLabel = new JLabel();

    // Dropdown panel to filter products by category
    private CategoryFilterPanel categoryFilterPanel;

    /**
     * Creates the DeleteProductPanel with a product table,
     * filter by category, and a delete button.
     */
    public DeleteProductPanel() {
        // Set layout and padding around panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //////////////////////////////
        // Category Filter Setup   //
        //////////////////////////////
        categoryFilterPanel = new CategoryFilterPanel(selectedCategory -> 
        {
            // Load all or filtered products based on selected category
            if ("All Categories".equals(selectedCategory)) 
            {
                ViewProductTable.loadAll(tableModel);
            } 
            else 
            {
                ViewProductTable.loadByCategory(tableModel, selectedCategory);
            }

            // Update message and reset selection
            messageLabel.setText("Filtered by category: " + selectedCategory);
            productTable.clearSelection();
        });

        // Add filter panel to top of layout
        add(categoryFilterPanel, BorderLayout.NORTH);

        //////////////////////////
        // Product Table Setup  //
        //////////////////////////

        // Set up table with headers and load initial data
        tableModel = new DefaultTableModel(ViewProductTable.getHeaders(), 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        ViewProductTable.loadAll(tableModel);

        //////////////////////
        // Delete Button    //
        //////////////////////
        deleteButton.addActionListener(e -> {
            // Get selected row index
            int selectedRow = productTable.getSelectedRow();

            // No row selected
            if (selectedRow == -1) {
                messageLabel.setText("Please select a product to delete.");
                return;
            }

            // Extract product details
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String productName = tableModel.getValueAt(selectedRow, 1).toString();

            // Ask user to confirm deletion
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete product: " + productName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            // If user cancels
            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Deletion cancelled by user.");
                return;
            }

            // Proceed with deletion
            try (Connection conn = MyConnection.getConnection()) 
            {
                int deleted = ProductDAO.deleteProduct(conn, productId);

                // Check if deletion was successful
                if (deleted > 0) {
                    messageLabel.setText("Product deleted successfully.");
                    ViewProductTable.loadAll(tableModel); // Refresh table
                    categoryFilterPanel.resetSelection(); // Reset filter
                } 
                else 
                {
                    messageLabel.setText("Deletion failed. Product may not exist.");
                }
            } 
            catch (Exception ex) 
            {
                // Handle database errors
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        ///////////////////////
        // Bottom Panel UI   //
        ///////////////////////

        // Bottom layout holding the delete button and message label
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(messageLabel);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add table and bottom panel to main panel
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
