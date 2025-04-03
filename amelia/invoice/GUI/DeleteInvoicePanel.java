package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.invoice.InvoiceDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

public class DeleteInvoicePanel extends JPanel {
    private JTable invoiceTable;
    private DefaultTableModel invoiceModel;
    private JButton deleteButton = new JButton("Delete Invoice");
    private JLabel messageLabel = new JLabel();

    private int selectedInvoiceId = -1;

    public DeleteInvoicePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table for invoices
        invoiceModel = new DefaultTableModel(ViewInvoiceTable.getHeaders(), 0);
        invoiceTable = new JTable(invoiceModel);
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Invoices"));

        ViewInvoiceTable.loadAll(invoiceModel);

        // Table selection
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.getSelectedRow();
                if (row != -1) {
                    selectedInvoiceId = (int) invoiceModel.getValueAt(row, 0);
                    messageLabel.setText("Invoice ID " + selectedInvoiceId + " selected.");
                }
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            if (selectedInvoiceId == -1) {
                messageLabel.setText("Please select an invoice to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this invoice and all its items?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                messageLabel.setText("Deletion cancelled.");
                return;
            }

            try (Connection conn = MyConnection.getConnection()) {
                InvoiceDAO dao = new InvoiceDAO(conn);
                boolean deleted = dao.deleteInvoiceWithItems(selectedInvoiceId);
                if (deleted) {
                    messageLabel.setText("Invoice deleted successfully.");
                    ViewInvoiceTable.loadAll(invoiceModel);
                    selectedInvoiceId = -1;
                } else {
                    messageLabel.setText("Failed to delete invoice.");
                }
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(deleteButton);
        bottomPanel.add(messageLabel);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
