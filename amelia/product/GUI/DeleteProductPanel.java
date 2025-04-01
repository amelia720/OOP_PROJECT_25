package amelia.product.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

import amelia.MyConnection;
import amelia.product.ProductDAO;

public class DeleteProductPanel extends JPanel {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton = new JButton("Delete Product");
    private JLabel messageLabel = new JLabel();
    private CategoryFilterPanel categoryFilterPanel;

    public DeleteProductPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //////////////////////////////
        // Category Filter Setup   //
        //////////////////////////////
        categoryFilterPanel = new CategoryFilterPanel(selectedCategory -> 
        {
            if ("All Categories".equals(selectedCategory)) 
            {
                ViewProductTable.loadAll(tableModel);
            } 
            else 
            {
                ViewProductTable.loadByCategory(tableModel, selectedCategory);
            }
            messageLabel.setText("Filtered by category: " + selectedCategory);
            productTable.clearSelection();
        });
        add(categoryFilterPanel, BorderLayout.NORTH);

        //////////////////////////
        // Product Table Setup  //
        //////////////////////////
        tableModel = new DefaultTableModel(ViewProductTable.getHeaders(), 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        ViewProductTable.loadAll(tableModel);

        //////////////////////
        // Delete Button    //
        //////////////////////
        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();

            if (selectedRow == -1) {
                messageLabel.setText("Please select a product to delete.");
                return;
            }

            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            String productName = tableModel.getValueAt(selectedRow, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete product: " + productName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Deletion cancelled by user.");
                return;
            }

            try (Connection conn = MyConnection.getConnection()) 
            {
                int deleted = ProductDAO.deleteProduct(conn, productId);
                if (deleted > 0) {
                    messageLabel.setText("Product deleted successfully.");
                    ViewProductTable.loadAll(tableModel); // Refresh table
                    categoryFilterPanel.resetSelection();
                } 
                else 
                {
                    messageLabel.setText("Deletion failed. Product may not exist.");
                }
            } 
            catch (Exception ex) 
            {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        ///////////////////////
        // Bottom Panel UI   //
        ///////////////////////
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(messageLabel);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
