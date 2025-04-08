package amelia.customer.GUI;

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

import amelia.customer.CustomerDAO;
import amelia.MyConnection;


/**
 * A panel for deleting customers through a GUI table.
 * Displays all customers and allows user to select and delete one.
 */
public class DeleteCustomerPanel extends JPanel 
{
    // Table to display customer list
    private JTable customerTable;

    // Table model holds the data shown in the table
    private DefaultTableModel tableModel;

    // Button to trigger customer deletion
    private JButton deleteButton = new JButton("Delete Customer");

    // Label to show success or error messages
    private JLabel messageLabel = new JLabel();

    /**
     * Constructor sets up the UI components and logic.
     */
    public DeleteCustomerPanel() {
        // Set layout and padding for the main panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //////////////////////////
        // Customer Table Setup //
        //////////////////////////

        // Create table model with column headers
        tableModel = new DefaultTableModel(ViewCustomerTable.getHeaders(), 0);

        // Create table and place it in a scrollable pane
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Load customer data into the table
        ViewCustomerTable.loadAll(tableModel);

        //////////////////////
        // Delete Button    //
        //////////////////////

        // Add an event listener to the delete button
        deleteButton.addActionListener(e -> {
            // Get the row that the user selected
            int selectedRow = customerTable.getSelectedRow();

            // If no row is selected, show a message
            if (selectedRow == -1) {
                messageLabel.setText("Please select a customer to delete.");
                return;
            }

            // Get customer ID and name from selected row
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            String fullName = tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

            // Show a confirmation dialog
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete customer: " + fullName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
            );

            // If user cancels, show message and stop
            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Deletion cancelled by user.");
                return;
            }

            // Try to connect to the database and delete the selected customer
            try (Connection conn = MyConnection.getConnection()) {
                int deleted = CustomerDAO.deleteCustomer(conn, customerId);

                // If deleted, refresh the table
                if (deleted > 0) {
                    messageLabel.setText("Customer deleted successfully.");
                    ViewCustomerTable.loadAll(tableModel); // Refresh table
                } else {
                    messageLabel.setText("Deletion failed. Customer may not exist.");
                }
            } catch (Exception ex) {
                // Show error message if something goes wrong
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        ///////////////////////
        // Bottom Panel UI   //
        ///////////////////////

        // Panel to hold the delete button and message label
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(deleteButton);
        buttonPanel.add(messageLabel);

        // Add button panel to the bottom of the UI
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add components to the main panel
        add(scrollPane, BorderLayout.CENTER); // Table goes in the center
        add(bottomPanel, BorderLayout.SOUTH); // Buttons and message go at the bottom
    }
}
