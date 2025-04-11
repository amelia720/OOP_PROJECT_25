package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.invoice.InvoiceDAO;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

/**
 * A panel for deleting invoices using a GUI.
 * Allows the user to select an invoice from a table and delete it.
 */
public class DeleteInvoicePanel extends JPanel 
{
    // Table to show invoices
    private JTable invoiceTable;

    // Table model to manage invoice data
    private DefaultTableModel invoiceModel;

    // Button to trigger deletion
    private JButton deleteButton = new JButton("Delete Invoice");

    // Label to show status messages
    private JLabel messageLabel = new JLabel();

    // Holds the selected invoice ID from the table
    private int selectedInvoiceId = -1;

    /**
     * Constructor sets up the panel layout, table, and button actions.
     */
    public DeleteInvoicePanel() 
    {
        // Set the layout and padding of the main panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ////////////////////////////
        // Invoice Table Setup    //
        ////////////////////////////

        // Initialize the table model with column headers
        invoiceModel = new DefaultTableModel(ViewInvoiceTable.getHeaders(), 0);
        invoiceTable = new JTable(invoiceModel);
        
        // Put the table in a scrollable pane with a title
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Invoices"));

        // Load all invoices into the table
        ViewInvoiceTable.loadAll(invoiceModel);

        ////////////////////////////////
        // Table Row Selection Logic  //
        ////////////////////////////////

        // Only allow one row to be selected at a time
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listen for mouse clicks on the table rows
        invoiceTable.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                int row = invoiceTable.getSelectedRow();

                if (row != -1) 
                {
                    // Get the ID of the selected invoice
                    selectedInvoiceId = (int) invoiceModel.getValueAt(row, 0);

                    // Display selected invoice ID in the message label
                    messageLabel.setText("Invoice ID " + selectedInvoiceId + " selected.");
                }
            }
        });

        ////////////////////////////////
        // Delete Button Functionality//
        ////////////////////////////////

        deleteButton.addActionListener(e -> {
            // If no invoice is selected, show a message
            if (selectedInvoiceId == -1) {
                messageLabel.setText("Please select an invoice to delete.");
                return;
            }

            // Confirm with the user before deletion
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this invoice and all its items?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            // If user cancels, exit
            if (confirm != JOptionPane.YES_OPTION) 
            {
                messageLabel.setText("Deletion cancelled.");
                return;
            }

            // Try deleting the invoice
            try (Connection conn = MyConnection.getConnection()) 
            {
                InvoiceDAO dao = new InvoiceDAO(conn);
                boolean deleted = dao.deleteInvoiceWithItems(selectedInvoiceId);

                if (deleted) 
                {
                    messageLabel.setText("Invoice deleted successfully.");

                    // Refresh the table
                    ViewInvoiceTable.loadAll(invoiceModel);
                    selectedInvoiceId = -1; // Reset selection
                } 
                else 
                {
                    messageLabel.setText("Failed to delete invoice.");
                }
            } 
            catch (Exception ex) 
            {
                messageLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace(); // Log the error to console
            }
        });

        ////////////////////////////////
        // Bottom Panel (Buttons + Msg)
        ////////////////////////////////

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(deleteButton);
        bottomPanel.add(messageLabel);

        // Add all components to the main panel
        add(scrollPane, BorderLayout.CENTER);     // Table in center
        add(bottomPanel, BorderLayout.SOUTH);     // Buttons at the bottom
    }
}
