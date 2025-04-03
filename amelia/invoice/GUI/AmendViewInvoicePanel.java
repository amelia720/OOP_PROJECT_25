package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.invoice.Invoice;
import amelia.invoice.InvoiceDAO;
import amelia.invoice.InvoiceItem;
import amelia.product.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

public class AmendViewInvoicePanel extends JPanel {
    private JTable invoiceTable;
    private JTable itemTable;
    private DefaultTableModel invoiceModel;
    private DefaultTableModel itemModel;

    private JTextField quantityField = new JTextField(5);
    private JButton updateButton = new JButton("Update Quantity");
    private JButton deleteButton = new JButton("Delete Item");
    private JLabel messageLabel = new JLabel();

    private JComboBox<Product> productComboBox = new JComboBox<>();
    private JTextField newQtyField = new JTextField(5);
    private JButton addButton = new JButton("Add Item");

    private int selectedInvoiceId = -1;
    private int selectedProductId = -1;

    public AmendViewInvoicePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tables
        invoiceModel = new DefaultTableModel(ViewInvoiceTable.getHeaders(), 0);
        invoiceTable = new JTable(invoiceModel);
        JScrollPane invoiceScroll = new JScrollPane(invoiceTable);
        invoiceScroll.setBorder(BorderFactory.createTitledBorder("Invoice List"));
        ViewInvoiceTable.loadAll(invoiceModel);

        itemModel = new DefaultTableModel(
                new String[]{"Product ID", "Product", "Category", "Unit Price", "Quantity", "Total"}, 0);
        itemTable = new JTable(itemModel);
        JScrollPane itemScroll = new JScrollPane(itemTable);
        itemScroll.setBorder(BorderFactory.createTitledBorder("Invoice Items"));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(invoiceScroll);
        centerPanel.add(itemScroll);

        // Event Listeners
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.getSelectedRow();
                if (row != -1) {
                    selectedInvoiceId = (int) invoiceModel.getValueAt(row, 0);
                    loadInvoiceItems(selectedInvoiceId);
                    loadProductDropdown();
                    messageLabel.setText("Invoice selected. You can now modify it.");
                }
            }
        });

        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = itemTable.getSelectedRow();
                if (row != -1) {
                    selectedProductId = (int) itemModel.getValueAt(row, 0);
                    quantityField.setText(itemModel.getValueAt(row, 4).toString());
                }
            }
        });

        // Update section
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.add(new JLabel("Selected Item Quantity:"));
        updatePanel.add(quantityField);
        updatePanel.add(updateButton);
        updatePanel.add(deleteButton);

        updateButton.addActionListener(e -> updateQuantity());
        deleteButton.addActionListener(e -> deleteItem());

        // Add item section
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Add New Product:"));
        addPanel.add(productComboBox);
        addPanel.add(new JLabel("Qty:"));
        addPanel.add(newQtyField);
        addPanel.add(addButton);

        addButton.addActionListener(e -> addNewItem());

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(updatePanel);
        controls.add(Box.createVerticalStrut(10));
        controls.add(addPanel);
        controls.add(Box.createVerticalStrut(10));
        controls.add(messageLabel);

        add(centerPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    private void loadInvoiceItems(int invoiceId) {
        itemModel.setRowCount(0);
        try (Connection conn = MyConnection.getConnection()) {
            InvoiceDAO dao = new InvoiceDAO(conn);
            Invoice invoice = dao.getInvoiceById(invoiceId);
            List<InvoiceItem> items = invoice.getItems();

            for (InvoiceItem item : items) {
                itemModel.addRow(new Object[]{
                        item.getProductId(),
                        item.getProductName(),
                        item.getCategory(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getTotalAmount()
                });
            }
        } catch (Exception ex) {
            messageLabel.setText("Failed to load invoice items.");
            ex.printStackTrace();
        }
    }

    private void loadProductDropdown() {
        try (Connection conn = MyConnection.getConnection()) {
            InvoiceDAO dao = new InvoiceDAO(conn);
            productComboBox.removeAllItems();
            for (Product p : dao.getAllProducts()) {
                productComboBox.addItem(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load products.");
        }
    }

    private void updateQuantity() {
        if (selectedInvoiceId == -1 || selectedProductId == -1) {
            messageLabel.setText("Select an invoice and item first.");
            return;
        }

        try (Connection conn = MyConnection.getConnection()) {
            int newQty = Integer.parseInt(quantityField.getText().trim());
            InvoiceDAO dao = new InvoiceDAO(conn);
            boolean updated = dao.updateInvoiceItemQuantity(selectedInvoiceId, selectedProductId, newQty);
            if (updated) {
                messageLabel.setText("Quantity updated.");
                ViewInvoiceTable.loadAll(invoiceModel);
                loadInvoiceItems(selectedInvoiceId);
            } else {
                messageLabel.setText("Update failed.");
            }
        } catch (Exception ex) {
            messageLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteItem() {
        if (selectedInvoiceId == -1 || selectedProductId == -1) {
            messageLabel.setText("Select an invoice and item to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = MyConnection.getConnection()) {
            InvoiceDAO dao = new InvoiceDAO(conn);
            boolean deleted = dao.deleteInvoiceItem(selectedInvoiceId, selectedProductId);
            if (deleted) {
                messageLabel.setText("Item deleted successfully.");
                ViewInvoiceTable.loadAll(invoiceModel);
                loadInvoiceItems(selectedInvoiceId);
            } else {
                messageLabel.setText("Delete failed.");
            }
        } catch (Exception ex) {
            messageLabel.setText("Error deleting item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void addNewItem() {
        if (selectedInvoiceId == -1) {
            messageLabel.setText("Select an invoice first.");
            return;
        }

        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            messageLabel.setText("No product selected.");
            return;
        }

        try (Connection conn = MyConnection.getConnection()) {
            int quantity = Integer.parseInt(newQtyField.getText().trim());
            InvoiceDAO dao = new InvoiceDAO(conn);
            dao.addInvoiceItem(selectedInvoiceId, selectedProduct.getProductId(), selectedProduct.getPrice(), quantity);
            messageLabel.setText("Item added successfully.");
            loadInvoiceItems(selectedInvoiceId);
            ViewInvoiceTable.loadAll(invoiceModel);
        } catch (Exception ex) {
            messageLabel.setText("Error adding item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
