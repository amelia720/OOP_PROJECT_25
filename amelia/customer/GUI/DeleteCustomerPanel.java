package amelia.customer.GUI;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

import amelia.customer.CustomerDAO;
import amelia.MyConnection;

public class DeleteCustomerPanel extends JPanel 
{

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton = new JButton("Delete Customer");
    private JLabel messageLabel = new JLabel();

    public DeleteCustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //////////////////////////
        // Customer Table Setup //
        //////////////////////////
        tableModel = new DefaultTableModel(ViewCustomerTable.getHeaders(), 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        ViewCustomerTable.loadAll(tableModel);

        //////////////////////
        // Delete Button    //
        //////////////////////
        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();

            if (selectedRow == -1) {
                messageLabel.setText("Please select a customer to delete.");
                return;
            }

            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            String fullName = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete customer: " + fullName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Deletion cancelled by user.");
                return;
            }

            try (Connection conn = MyConnection.getConnection()) {
                int deleted = CustomerDAO.deleteCustomer(conn, customerId);
                if (deleted > 0) {
                    messageLabel.setText("Customer deleted successfully.");
                    ViewCustomerTable.loadAll(tableModel); // Refresh table
                } else {
                    messageLabel.setText("Deletion failed. Customer may not exist.");
                }
            } catch (Exception ex) {
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
