package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.invoice.Invoice;
import amelia.invoice.InvoiceDAO;
import amelia.invoice.InvoiceItem;
import amelia.product.Product;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.util.List;

/**
 * GUI panel to view and modify invoice data:
 * - Select an invoice
 * - View, update, delete, or add invoice items
 */
public class AmendViewInvoicePanel extends JPanel 
{

    // Tables to show invoice list and invoice items
    private JTable invoiceTable;
    private JTable itemTable;

    // Models that hold the data for the tables
    private DefaultTableModel invoiceModel;
    private DefaultTableModel itemModel;

    // Fields and buttons for editing items
    private JTextField quantityField = new JTextField(5);
    private JButton updateButton = new JButton("Update Quantity");
    private JButton deleteButton = new JButton("Delete Item");
    private JLabel messageLabel = new JLabel(); // Displays messages to the user

    // Fields for adding a new item to invoice
    private JComboBox<Product> productComboBox = new JComboBox<>();
    private JTextField newQtyField = new JTextField(5);
    private JButton addButton = new JButton("Add Item");

    // Tracks what invoice and product are selected
    private int selectedInvoiceId = -1;
    private int selectedProductId = -1;

    /**
     * Constructor sets up layout, components, and event listeners.
     */
    public AmendViewInvoicePanel() 
    {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ///////////////////////////////////////
        // Invoice Table + ScrollPane Setup  //
        ///////////////////////////////////////
        invoiceModel = new DefaultTableModel(ViewInvoiceTable.getHeaders(), 0);
        invoiceTable = new JTable(invoiceModel);
        JScrollPane invoiceScroll = new JScrollPane(invoiceTable);
        invoiceScroll.setBorder(BorderFactory.createTitledBorder("Invoice List"));
        ViewInvoiceTable.loadAll(invoiceModel); // Load all invoices from DB

        /////////////////////////////////////
        // Item Table + ScrollPane Setup   //
        /////////////////////////////////////
        itemModel = new DefaultTableModel(new String[]{"Product ID", "Product", "Category", "Unit Price", "Quantity", "Total"}, 0);
        itemTable = new JTable(itemModel);
        JScrollPane itemScroll = new JScrollPane(itemTable);
        itemScroll.setBorder(BorderFactory.createTitledBorder("Invoice Items"));

        ///////////////////////////////////
        // Tables Panel (top half)       //
        ///////////////////////////////////
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.add(invoiceScroll);
        tablesPanel.add(itemScroll);

        ////////////////////////////////
        // Panel for Updating Items   //
        ////////////////////////////////
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Modify Selected Item"));
        updatePanel.add(new JLabel("Quantity:"));
        updatePanel.add(quantityField);
        updatePanel.add(updateButton);
        updatePanel.add(deleteButton);

        ////////////////////////////////
        // Panel for Adding New Items //
        ////////////////////////////////
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add New Product to Invoice"));
        addPanel.add(productComboBox);
        addPanel.add(new JLabel("Qty:"));
        addPanel.add(newQtyField);
        addPanel.add(addButton);

        ///////////////////////////////////
        // Bottom Section (actions + msg)//
        ///////////////////////////////////
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.add(updatePanel); // Top: update item
        actionsPanel.add(Box.createVerticalStrut(10));
        actionsPanel.add(addPanel);    // Below: add new item
        bottomPanel.add(actionsPanel, BorderLayout.CENTER);
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        ////////////////////
        // Add to Layout  //
        ////////////////////
        add(tablesPanel, BorderLayout.CENTER);     // Tables (top)
        add(bottomPanel, BorderLayout.SOUTH);      // Buttons (bottom)

        ////////////////////////////////////
        // Event Listeners for Interaction//
        ////////////////////////////////////

        // When a user selects an invoice row
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.getSelectedRow();
                if (row != -1) 
                {
                    selectedInvoiceId = (int) invoiceModel.getValueAt(row, 0);
                    loadInvoiceItems(selectedInvoiceId); // Load its items
                    loadProductDropdown();               // Load product options
                    messageLabel.setText("Invoice selected. You can now modify it.");
                }
            }
        });

        // When a user selects a row from item table
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                int row = itemTable.getSelectedRow();
                if (row != -1) 
                {
                    selectedProductId = (int) itemModel.getValueAt(row, 0);
                    quantityField.setText(itemModel.getValueAt(row, 4).toString());
                }
            }
        });

        // Button actions
        updateButton.addActionListener(e -> updateQuantity());
        deleteButton.addActionListener(e -> deleteItem());
        addButton.addActionListener(e -> addNewItem());
    }

    /**
     * Load all items for a specific invoice into the item table.
     */
    private void loadInvoiceItems(int invoiceId) 
    {
        itemModel.setRowCount(0); // Clear existing rows

        try (Connection conn = MyConnection.getConnection()) 
        {
            InvoiceDAO dao = new InvoiceDAO(conn);
            Invoice invoice = dao.getInvoiceById(invoiceId);
            List<InvoiceItem> items = invoice.getItems();

            for (InvoiceItem item : items) 
            {
                itemModel.addRow(new Object[]
                {
                        item.getProductId(),
                        item.getProductName(),
                        item.getCategory(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getTotalAmount()
                });
            }
        } catch (Exception ex) 
        {
            messageLabel.setText("Failed to load invoice items.");
            ex.printStackTrace();
        }
    }

    /**
     * Load all products into the combo box (used when adding new items).
     */
    private void loadProductDropdown() 
    {
        try (Connection conn = MyConnection.getConnection()) 
        {
            InvoiceDAO dao = new InvoiceDAO(conn);
            productComboBox.removeAllItems();
            for (Product p : dao.getAllProducts()) 
            {
                productComboBox.addItem(p);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            messageLabel.setText("Failed to load products.");
        }
    }

    /**
     * Updates the quantity of an existing invoice item.
     */
    private void updateQuantity() 
    {
        if (selectedInvoiceId == -1 || selectedProductId == -1) 
        {
            messageLabel.setText("Select an invoice and item first.");
            return;
        }

        try (Connection conn = MyConnection.getConnection()) 
        {
            int newQty = Integer.parseInt(quantityField.getText().trim());
            InvoiceDAO dao = new InvoiceDAO(conn);
            boolean updated = dao.updateInvoiceItemQuantity(selectedInvoiceId, selectedProductId, newQty);

            if (updated) 
            {
                messageLabel.setText("Quantity updated.");
                ViewInvoiceTable.loadAll(invoiceModel);
                loadInvoiceItems(selectedInvoiceId);
            } 
            else 
            {
                messageLabel.setText("Update failed.");
            }
        } 
        catch (Exception ex) 
        {
            messageLabel.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Deletes an item from the selected invoice.
     */
    private void deleteItem() 
    {
        if (selectedInvoiceId == -1 || selectedProductId == -1) 
        {
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

        try (Connection conn = MyConnection.getConnection()) 
        {
            InvoiceDAO dao = new InvoiceDAO(conn); // DAO to interact with DB
            boolean deleted = dao.deleteInvoiceItem(selectedInvoiceId, selectedProductId); // Delete item

            if (deleted) // If deletion was successful
            {
                messageLabel.setText("Item deleted successfully."); // Show success message
                ViewInvoiceTable.loadAll(invoiceModel); // Reload all invoices
                loadInvoiceItems(selectedInvoiceId); // Reload items for the selected invoice
            } 
            else 
            {
                messageLabel.setText("Delete failed.");
            }
        } 
        catch (Exception ex) 
        {
            messageLabel.setText("Error deleting item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Adds a new product to the selected invoice.
     */
    private void addNewItem() 
    {
        if (selectedInvoiceId == -1) 
        {
            messageLabel.setText("Select an invoice first.");
            return;
        }

        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) 
        {
            messageLabel.setText("No product selected.");
            return;
        }

        try (Connection conn = MyConnection.getConnection()) 
        {
            int quantity = Integer.parseInt(newQtyField.getText().trim());
            InvoiceDAO dao = new InvoiceDAO(conn);
            dao.addInvoiceItem(selectedInvoiceId, selectedProduct.getProductId(), selectedProduct.getPrice(), quantity);

            messageLabel.setText("Item added successfully.");
            loadInvoiceItems(selectedInvoiceId);
            ViewInvoiceTable.loadAll(invoiceModel);
        } 
        catch (Exception ex) 
        {
            messageLabel.setText("Error adding item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
