package amelia.invoice.GUI;

import amelia.MyConnection;
import amelia.customer.Customer;
import amelia.invoice.InvoiceDAO;
import amelia.invoice.InvoiceItem;
import amelia.invoice.InvoicePanel;
import amelia.product.Product;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class AddInvoicePanel extends JPanel {

    public AddInvoicePanel() {
        setLayout(new BorderLayout(10, 10));
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try (Connection conn = MyConnection.getConnection()) {
            InvoiceDAO dao = new InvoiceDAO(conn);
            List<Customer> customers = dao.getAllCustomers();
            List<Product> products = dao.getAllProducts();

            InvoicePanel invoicePanel = new InvoicePanel(customers, products);
            JButton insertButton = new JButton("Insert Invoice");

            insertButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to insert this invoice?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) {
                    messageLabel.setText("Insertion cancelled.");
                    return;
                }

                try (Connection conn2 = MyConnection.getConnection()) {
                    InvoiceDAO invoiceDAO = new InvoiceDAO(conn2);

                    Customer selectedCustomer = invoicePanel.getSelectedCustomer();
                    List<InvoiceItem> items = invoicePanel.getInvoiceItems();

                    if (selectedCustomer == null || items.isEmpty()) {
                        messageLabel.setText("Customer and at least one item are required.");
                        return;
                    }

                    int invoiceId = invoiceDAO.createInvoice(selectedCustomer.getCustomerId());
                    for (InvoiceItem item : items) {
                        invoiceDAO.addInvoiceItem(invoiceId, item.getProductId(), item.getUnitPrice(), item.getQuantity());
                    }

                    invoicePanel.clearForm();
                    messageLabel.setText("Invoice #" + invoiceId + " inserted successfully!");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    messageLabel.setText("Error: " + ex.getMessage());
                }
            });

            add(invoicePanel, BorderLayout.CENTER);
            add(insertButton, BorderLayout.SOUTH);
            add(messageLabel, BorderLayout.NORTH);

        } catch (Exception ex) {
            ex.printStackTrace();
            add(new JLabel("Failed to load data: " + ex.getMessage()), BorderLayout.CENTER);
        }
    }
}
