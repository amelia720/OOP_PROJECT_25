package amelia.invoice;

import amelia.customer.Customer;
import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int invoiceId;
    private LocalDateTime invoiceDate;
    private Customer customer;
    private List<InvoiceItem> items;

    // Constructors
    public Invoice() {}

    public Invoice(int invoiceId, LocalDateTime invoiceDate, Customer customer, List<InvoiceItem> items) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.customer = customer;
        this.items = items;
    }

    // Getters
    public int getInvoiceId() {
        return invoiceId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    // Setters
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    // Get total amount
    public double getTotalAmount() {
        return items.stream().mapToDouble(InvoiceItem::getTotalAmount).sum();
    }

    // Display the invoice
    public void displayInvoice() {
        System.out.println("======================================");
        System.out.println("           INVOICE #" + invoiceId);
        System.out.println("======================================");
        System.out.println("Date: " + invoiceDate);
        System.out.println("\nCustomer:");
        customer.displayCustomerInfo();

        System.out.println("\nItems:");
        for (InvoiceItem item : items) {
            System.out.println(" - " + item);
        }

        System.out.printf("\nTOTAL AMOUNT: €%.2f%n", getTotalAmount());
        System.out.println("======================================\n");
    }
}
