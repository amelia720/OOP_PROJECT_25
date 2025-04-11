package amelia.customer.GUI;

// Import all required Swing and AWT classes
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;

// Table model
import javax.swing.table.DefaultTableModel;

// AWT layout and event handling
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import amelia.customer.CustomerPanel;
import amelia.customer.Customer;
import amelia.customer.CustomerDAO;

// JDBC connection
import java.sql.Connection;
import amelia.MyConnection;

/**
 * GUI panel that lets the user view and edit customer information from a table.
 */
public class AmendViewCustomerPanel extends CustomerPanel 
{
    // Table to display customers
    private JTable customerTable;

    // Model that holds the table data
    private DefaultTableModel tableModel;

    // Button to update customer info
    private JButton updateButton = new JButton("Update Customer");

    // Label to show messages (success or error)
    private JLabel messageLabel = new JLabel();

    // Tracks which customer is currently selected in the table
    private int selectedCustomerId = -1;

    /**
     * Constructor that sets up the table, form, and update logic.
     */
    public AmendViewCustomerPanel() 
    {
        super(); // Build form layout using CustomerPanel (adds fields + labels)

        // Use BorderLayout with 10px gaps between components
        setLayout(new BorderLayout(10, 10));

        // Add 15px padding around the panel
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        ////////////////////////////
        // Customer Table Section //
        ////////////////////////////

        // Set up the table model with column headers
        tableModel = new DefaultTableModel(ViewCustomerTable.getHeaders(), 0);

        // Create the table and put it in a scrollable panel
        customerTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        // Load all customers into the table
        ViewCustomerTable.loadAll(tableModel);

        // Allow only one row to be selected at a time
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a mouse listener to the customer table to handle row clicks
        customerTable.addMouseListener( // When a row is clicked, load that customer's data into the form fields
            new MouseAdapter() // Create a new object of a class that extends MouseAdapter 
            // I use MouseAdapter so we only need to override the method we care about
            {
                // This method runs when the user clicks on a row in the table
                public void mouseClicked(MouseEvent e) 
                {
                    // Get the index of the selected row
                    int row = customerTable.getSelectedRow(); // method from JTable

                    // Make sure a valid row was actually clicked
                    if (row != -1) 
                    {
                        // Get customer ID from the selected row (column 0)
                        selectedCustomerId = (int) tableModel.getValueAt(row, 0); // method from DefaultTableModel

                        // Create a new Customer object and fill it with values from the table row
                        Customer customer = new Customer();
                        customer.setCustomerId(selectedCustomerId);
                        customer.setFname(tableModel.getValueAt(row, 1).toString());
                        customer.setSname(tableModel.getValueAt(row, 2).toString());
                        customer.setAddress(tableModel.getValueAt(row, 3).toString());
                        customer.setEmail(tableModel.getValueAt(row, 4).toString());
                        customer.setPhone(tableModel.getValueAt(row, 5).toString());

                        // Fill the form fields with the customer’s data
                        setCustomerData(customer);

                        // Show message to confirm the customer is loaded
                        messageLabel.setText("Customer loaded. You can now edit.");
                    }
                }
            }
        ); // End of addMouseListener method call


        ///////////////////////////////////
        // Form Panel + Button Panel     //
        ///////////////////////////////////

        // Create a panel to hold the inherited form fields
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.add(new JLabel("First Name:")); formPanel.add(fnameField);
        formPanel.add(new JLabel("Surname:"));    formPanel.add(snameField);
        formPanel.add(new JLabel("Address:"));    formPanel.add(addressField);
        formPanel.add(new JLabel("Email:"));      formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));      formPanel.add(phoneField);

        // Create a panel for the update button and message label
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(updateButton);
        buttonPanel.add(messageLabel);

        // Combine form and button into one panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER); // Add form fields
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH); // Add update button + message

        // Add everything to the main layout
        add(scrollPane, BorderLayout.CENTER);     // Table at the top
        add(bottomPanel, BorderLayout.SOUTH);     // Form and button at the bottom

        ////////////////////
        // Update Button  //
        ////////////////////

        // Add a click event to the update button
        updateButton.addActionListener
        (e -> 
            {
                // If no customer is selected, show a message and stop
                if (selectedCustomerId == -1) 
                {
                    messageLabel.setText("Please select a customer first.");
                    return;
                }

                // Ask the user to confirm the update
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to update this customer?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION
                );

                // If user says no, cancel the update
                if (confirm != JOptionPane.YES_OPTION) 
                {
                    messageLabel.setText("Update cancelled by user.");
                    return;
                }

                try 
                {
                    // Get the updated info from the form
                    Customer customer = getCustomerData();
                    customer.setCustomerId(selectedCustomerId);

                    // Connect to the database
                    Connection conn = MyConnection.getConnection();

                    // If the connection was successful
                    if (conn != null) 
                    {
                        try 
                        {
                            // Try updating the customer in the database through DAO
                            int updated = CustomerDAO.updateCustomer(conn, customer);

                            if (updated > 0) 
                            {
                                messageLabel.setText("Customer updated successfully.");

                                // Reload table data to reflect changes
                                ViewCustomerTable.loadAll(tableModel);

                                // Reset form and selection
                                setCustomerData(new Customer());
                                customerTable.clearSelection(); // method from JTable to clear selection
                                selectedCustomerId = -1; // reset selected customer ID
                            } 
                            else 
                            {
                                // If update failed, show a message
                                messageLabel.setText("Update failed.");
                            }
                        } 
                        finally 
                        {
                            // Always close the database connection
                            conn.close();
                        }
                    } 
                    else 
                    {
                        // If connection failed, show a message
                        messageLabel.setText("Database connection failed.");
                    }

                } 
                catch (IllegalArgumentException ex) 
                {
                    // If the input is invalid, show validation error
                    messageLabel.setText("Validation Error: " + ex.getMessage());

                } 
                catch (Exception ex) 
                {
                    // Catch any other unexpected errors
                    messageLabel.setText("Error: " + ex.getMessage());
                }
            }
        ); // End of updateButton.addActionListener
    }
}
