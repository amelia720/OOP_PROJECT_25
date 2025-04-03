package amelia.invoice;

import amelia.customer.Customer;
import amelia.product.Product;
import amelia.invoice.InvoiceItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InvoicePanel extends JPanel {

    private JComboBox<Customer> customerBox = new JComboBox<>();
    private JComboBox<Product> productBox = new JComboBox<>();
    private JTextField quantityField = new JTextField();

    private DefaultListModel<InvoiceItem> itemListModel = new DefaultListModel<>();
    private JList<InvoiceItem> itemList = new JList<>(itemListModel);

    private JButton addItemButton = new JButton("Add Item");

    public InvoicePanel(List<Customer> customers, List<Product> products) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.add(new JLabel("Select Customer:"));
        for (Customer c : customers) customerBox.addItem(c);
        formPanel.add(customerBox);

        formPanel.add(new JLabel("Select Product:"));
        for (Product p : products) productBox.addItem(p);
        formPanel.add(productBox);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        // Add item button
        addItemButton.addActionListener(e -> {
            try {
                Product product = (Product) productBox.getSelectedItem();
                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity <= 0) 
                {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                    return;
                }

                if (quantity > product.getStock()) 
                {
                    JOptionPane.showMessageDialog(this,
                        "Cannot add more than available stock (" + product.getStock() + " in stock).",
                        "Stock Limit",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (quantity <= 0) 
                {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                    return;
                }

                InvoiceItem item = new InvoiceItem(
                    product.getProductId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    quantity
                );

                itemListModel.addElement(item);
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.");
            }
        });

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JLabel("Invoice Items:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(addItemButton, BorderLayout.CENTER);
        add(listPanel, BorderLayout.SOUTH);
    }

    public Customer getSelectedCustomer() {
        return (Customer) customerBox.getSelectedItem();
    }

    public List<InvoiceItem> getInvoiceItems() {
        List<InvoiceItem> items = new ArrayList<>();
        for (int i = 0; i < itemListModel.size(); i++) {
            items.add(itemListModel.get(i));
        }
        return items;
    }

    public void clearForm() {
        quantityField.setText("");
        itemListModel.clear();
    }
}
