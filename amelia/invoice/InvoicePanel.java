package amelia.invoice;

import amelia.customer.Customer;
import amelia.product.Product;
// import amelia.invoice.InvoiceItem;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom panel used to create an invoice.
 * Allows selecting a customer, choosing products, entering quantity,
 * and adding items to a list.
 */
public class InvoicePanel extends JPanel 
{

    // Dropdown for selecting a customer
    private JComboBox<Customer> customerBox = new JComboBox<>();

    // Dropdown for selecting a product
    private JComboBox<Product> productBox = new JComboBox<>();

    // Text field for entering quantity
    private JTextField quantityField = new JTextField();

    // List model and JList for displaying invoice items
    private DefaultListModel<InvoiceItem> itemListModel = new DefaultListModel<>();
    private JList<InvoiceItem> itemList = new JList<>(itemListModel);

    // Button to add item to invoice
    private JButton addItemButton = new JButton("Add Item");

    /**
     * Constructor sets up the form and item list using the customer and product data.
     */
    public InvoicePanel(List<Customer> customers, List<Product> products) 
    {
        setLayout(new BorderLayout(10, 10)); // Main layout for the panel
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around panel

        /////////////////////////
        // Form Panel Section  //
        /////////////////////////
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Form layout

        // Add customer dropdown
        formPanel.add(new JLabel("Select Customer:"));
        for (Customer c : customers) customerBox.addItem(c);
        formPanel.add(customerBox);

        // Add product dropdown
        formPanel.add(new JLabel("Select Product:"));
        for (Product p : products) productBox.addItem(p);
        formPanel.add(productBox);

        // Add quantity input
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        ///////////////////////////////
        // Add Item Button Logic     //
        ///////////////////////////////
        addItemButton.addActionListener(e -> 
        {
            try 
            {
                Product product = (Product) productBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());

                // Check quantity is positive
                if (quantity <= 0) 
                {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                    return;
                }

                // Check if there's enough stock
                if (quantity > product.getStock()) 
                {
                    JOptionPane.showMessageDialog(this,
                        "Cannot add more than available stock (" + product.getStock() + " in stock).",
                        "Stock Limit",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Create an InvoiceItem and add it to the list
                InvoiceItem item = new InvoiceItem(
                    product.getProductId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    quantity
                );

                itemListModel.addElement(item); // Add to JList
                quantityField.setText(""); // Clear quantity input
            } 
            catch (NumberFormatException ex) 
            {
                // If quantity is not a number
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            }
        });

        /////////////////////////
        // Invoice Item List   //
        /////////////////////////
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Invoice Items:"), BorderLayout.NORTH); // Label
        listPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);   // Scrollable list

        /////////////////////////
        // Add Components      //
        /////////////////////////
        add(formPanel, BorderLayout.NORTH);         // Form section
        add(addItemButton, BorderLayout.CENTER);    // Add item button
        add(listPanel, BorderLayout.SOUTH);         // List of invoice items
    }

    /**
     * Returns the customer selected in the dropdown.
     */
    public Customer getSelectedCustomer() 
    {
        return (Customer) customerBox.getSelectedItem();
    }

    /**
     * Returns all the invoice items added to the list.
     */
    public List<InvoiceItem> getInvoiceItems() 
    {
        List<InvoiceItem> items = new ArrayList<>();
        for (int i = 0; i < itemListModel.size(); i++) 
        {
            items.add(itemListModel.get(i));
        }
        return items;
    }

    /**
     * Clears the quantity input and the invoice item list.
     */
    public void clearForm() 
    {
        quantityField.setText("");
        itemListModel.clear();
    }
}
