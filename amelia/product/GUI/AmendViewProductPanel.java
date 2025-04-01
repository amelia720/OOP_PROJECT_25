package amelia.product.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import amelia.product.Product;
import amelia.product.ProductDAO;
import amelia.product.ProductPanel;
import amelia.MyConnection;

public class AmendViewProductPanel extends ProductPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton updateButton = new JButton("Update Product");
    private JLabel messageLabel = new JLabel();
    private CategoryFilterPanel categoryFilterPanel;

    private int selectedProductId = -1;

    public AmendViewProductPanel() {
        // Layout setup
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ////////////////////////////
        // Category Filter Panel  //
        ////////////////////////////
        categoryFilterPanel = new CategoryFilterPanel(selectedCategory -> {
            if ("All Categories".equals(selectedCategory)) {
                ViewProductTable.loadAll(tableModel);
            } else {
                ViewProductTable.loadByCategory(tableModel, selectedCategory);
            }

            selectedProductId = -1;
            setProductData(new Product());
            productTable.clearSelection();
            messageLabel.setText("Filtered by category: " + selectedCategory);
        });
        add(categoryFilterPanel, BorderLayout.NORTH);

        //////////////////////////
        // Product Table Setup //
        //////////////////////////
        tableModel = new DefaultTableModel(ViewProductTable.getHeaders(), 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        ViewProductTable.loadAll(tableModel);

        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = productTable.getSelectedRow();
                if (row != -1) {
                    selectedProductId = (int) tableModel.getValueAt(row, 0);
                    Product product = new Product();
                    product.setProductId(selectedProductId);
                    product.setName(tableModel.getValueAt(row, 1).toString());
                    product.setCategory(tableModel.getValueAt(row, 2).toString());

                    try {
                        product.setPrice(Double.parseDouble(tableModel.getValueAt(row, 3).toString()));
                        product.setStock(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
                    } catch (NumberFormatException ex) {
                        messageLabel.setText("Error loading selected product: " + ex.getMessage());
                        return;
                    }

                    setProductData(product);
                    messageLabel.setText("Product loaded. You can now edit.");
                }
            }
        });

        ///////////////////////////////
        // Form + Button Layout UI   //
        ///////////////////////////////
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.add(new JLabel("Product Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Category:")); formPanel.add(categoryCombo);  // ComboBox used
        formPanel.add(new JLabel("Price:")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stock:")); formPanel.add(stockField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(updateButton);
        buttonPanel.add(messageLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        /////////////////////////////
        // Update Button Listener  //
        /////////////////////////////
        updateButton.addActionListener(e -> {
            if (selectedProductId == -1) {
                messageLabel.setText("Please select a product first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to update this product?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Update cancelled by user.");
                return;
            }

            Connection conn = null;

            try {
                Product product = getProductData();
                product.setProductId(selectedProductId);

                conn = MyConnection.getConnection();
                if (conn != null) {
                    int updated = ProductDAO.updateProduct(conn, product);
                    if (updated > 0) {
                        messageLabel.setText("Product updated successfully.");
                        ViewProductTable.loadAll(tableModel); // Refresh table

                        // Reset form & selection
                        setProductData(new Product());
                        productTable.clearSelection();
                        selectedProductId = -1;
                        categoryFilterPanel.resetSelection(); // optional: reset filter
                    } else {
                        messageLabel.setText("Update failed.");
                    }
                } else {
                    messageLabel.setText("Database connection failed.");
                }

            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter valid numbers for price and stock.");
            } catch (IllegalArgumentException ex) {
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
