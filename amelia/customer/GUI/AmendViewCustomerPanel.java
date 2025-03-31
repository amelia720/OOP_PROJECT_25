package amelia.customer.GUI;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import amelia.customer.CustomerPanel;
import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

import amelia.MyConnection;

public class AmendViewCustomerPanel extends CustomerPanel 
{

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton updateButton = new JButton("Update Customer");
    private JLabel messageLabel = new JLabel();

    private int selectedCustomerId = -1;

    public AmendViewCustomerPanel() {
        // Layout setup
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ////////////////////////////
        // Customer Table Section //
        ////////////////////////////
        tableModel = new DefaultTableModel(ViewCustomerTable.getHeaders(), 0);
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        ViewCustomerTable.loadAll(tableModel);

        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row != -1) {
                    selectedCustomerId = (int) tableModel.getValueAt(row, 0);
                    Customer customer = new Customer();
                    customer.setCustomerId(selectedCustomerId);
                    customer.setFname(tableModel.getValueAt(row, 1).toString());
                    customer.setSname(tableModel.getValueAt(row, 2).toString());
                    customer.setAddress(tableModel.getValueAt(row, 3).toString());
                    customer.setEmail(tableModel.getValueAt(row, 4).toString());
                    customer.setPhone(tableModel.getValueAt(row, 5).toString());
                    setCustomerData(customer);
                    messageLabel.setText("Customer loaded. You can now edit.");
                }
            }
        });

        ///////////////////////////////////
        // Form Panel + Button Panel     //
        ///////////////////////////////////
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("First Name:")); formPanel.add(fnameField);
        formPanel.add(new JLabel("Surname:")); formPanel.add(snameField);
        formPanel.add(new JLabel("Address:")); formPanel.add(addressField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(updateButton);
        buttonPanel.add(messageLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        ////////////////////
        // Update Button  //
        ////////////////////
        updateButton.addActionListener(e -> {
            if (selectedCustomerId == -1) {
                messageLabel.setText("Please select a customer first.");
                return;
            }

            // Confirmation popup
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to update this customer?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Update cancelled by user.");
                return;
            }

            try {
                Customer customer = getCustomerData();
                customer.setCustomerId(selectedCustomerId);

                Connection conn = MyConnection.getConnection();
                if (conn != null) {
                    try {
                        int updated = CustomerDAO.updateCustomer(conn, customer);
                        if (updated > 0) {
                            messageLabel.setText("Customer updated successfully.");
                            ViewCustomerTable.loadAll(tableModel); // Refresh table

                            // Reset selection & form
                            setCustomerData(new Customer());
                            customerTable.clearSelection();
                            selectedCustomerId = -1;
                        } else {
                            messageLabel.setText("Update failed.");
                        }
                    } finally {
                        conn.close();
                    }
                } else {
                    messageLabel.setText("Database connection failed.");
                }
            } catch (IllegalArgumentException ex) {
                messageLabel.setText("Validation Error: " + ex.getMessage());
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });
    }
}
