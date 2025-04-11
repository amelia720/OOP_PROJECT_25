package amelia.product.GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;

import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.product.ProductPanel;
import amelia.MyConnection;

/**
 * A panel that lets users view, filter, and update existing products.
 * <p>
 * Shows a table of all products. Users can select a product from the table,
 * see its details in a form, and update its information.
 * </p>
 */
public class AmendViewProductPanel extends ProductPanel 
{

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton updateButton = new JButton("Update Product");
    private JLabel messageLabel = new JLabel();
    private CategoryFilterPanel categoryFilterPanel;

    private int selectedProductId = -1;

    /**
     * Creates the panel to view and update products.
     * Sets up the table, filter panel, form, and update button.
     */
    public AmendViewProductPanel() 
    {
        // Set layout and padding for the panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ////////////////////////////
        // Category Filter Panel  //
        ////////////////////////////
        categoryFilterPanel = new CategoryFilterPanel(selectedCategory -> 
        {
            // Load products by category or show all
            if ("All Categories".equals(selectedCategory)) 
            {
                ViewProductTable.loadAll(tableModel);
            } 
            else 
            {
                ViewProductTable.loadByCategory(tableModel, selectedCategory);
            }

            // Reset selection and form
            selectedProductId = -1;
            setProductData(new Product());
            productTable.clearSelection();
            messageLabel.setText("Filtered by category: " + selectedCategory);
        });
        add(categoryFilterPanel, BorderLayout.NORTH);

        //////////////////////////
        // Product Table Setup  //
        //////////////////////////
        tableModel = new DefaultTableModel(ViewProductTable.getHeaders(), 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Load all products into the table initially
        ViewProductTable.loadAll(tableModel);

        // Only allow one row to be selected at a time
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load selected product into form when clicked
        productTable.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    selectedProductId = (int) tableModel.getValueAt(row, 0);

                    // Create Product and fill with table values
                    Product product = new Product();
                    product.setProductId(selectedProductId);
                    product.setName(tableModel.getValueAt(row, 1).toString());
                    product.setCategory(tableModel.getValueAt(row, 2).toString());

                    try 
                    {
                        product.setPrice(Double.parseDouble(tableModel.getValueAt(row, 3).toString()));
                        product.setStock(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
                    } 
                    catch (NumberFormatException ex) 
                    {
                        messageLabel.setText("Error loading selected product: " + ex.getMessage());
                        return;
                    }

                    // Show product details in form
                    setProductData(product);
                    messageLabel.setText("Product loaded. You can now edit.");
                }
            }
        });

        ///////////////////////////////
        // Form + Button Layout UI   //
        ///////////////////////////////

        // Manual layout for inherited form fields
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("Product Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Category:")); formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Price:")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stock:")); formPanel.add(stockField);

        // Panel for update button and messages
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(updateButton);
        buttonPanel.add(messageLabel);

        // Combine form and button panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add everything to the main panel
        add(scrollPane, BorderLayout.CENTER);   // Table at the center
        add(bottomPanel, BorderLayout.SOUTH);   // Form and buttons at bottom

        /////////////////////////////
        // Update Button Listener  //
        /////////////////////////////
        updateButton.addActionListener(e -> 
        {
            // Require a product to be selected
            if (selectedProductId == -1) 
            {
                messageLabel.setText("Please select a product first.");
                return;
            }

            // Confirm update
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to update this product?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Update cancelled by user.");
                return;
            }

            Connection conn = null;

            try 
            {
                // Get data from form and attach selected ID
                Product product = getProductData();
                product.setProductId(selectedProductId);

                conn = MyConnection.getConnection();
                if (conn != null) // connection successful
                {
                    int updated = ProductDAO.updateProduct(conn, product); // Update product in DB
                    if (updated > 0) // update successful
                    {
                        // Clear form and show success message
                        messageLabel.setText("Product updated successfully.");

                        // Reload table data and reset
                        ViewProductTable.loadAll(tableModel);
                        setProductData(new Product()); // clear form
                        productTable.clearSelection(); // clear selection
                        selectedProductId = -1; // reset selected ID
                        categoryFilterPanel.resetSelection(); // optional reset
                    } 
                    else 
                    {
                        // If update fails
                        messageLabel.setText("Update failed.");
                    }
                } 
                // If connection fails
                else 
                {
                    // Show error message
                    messageLabel.setText("Database connection failed."); 
                }

            } 
            // If user enters invalid number (e.g. letters instead of numbers)
            catch (NumberFormatException ex) 
            {
                // Show error message
                messageLabel.setText("Please enter valid numbers for price and stock.");
            } 
            // If product validation in the setter fails (e.g. negative price)
            catch (IllegalArgumentException ex) 
            {
                // Show error message
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } 
            // Catch any other unexpected error
            catch (Exception ex) 
            {
                messageLabel.setText("Error: " + ex.getMessage());
            } 
            finally 
            {
                // Always close the connection
                try 
                {
                    if (conn != null) conn.close(); // Always close connection
                } 
                catch (Exception ex)  // Connection close error
                {
                    ex.printStackTrace(); // Print error in console (for debugging)
                }
            }
        });
    }
}
