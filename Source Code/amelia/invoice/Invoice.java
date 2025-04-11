package amelia.invoice;

import amelia.customer.Customer;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an invoice that includes customer info,
 * a list of items, the invoice date, and total amount.
 */
public class Invoice 
{
    // Unique identifier for this invoice
    private int invoiceId;

    // The date and time the invoice was created
    private LocalDateTime invoiceDate;

    // The customer associated with this invoice
    private Customer customer;

    // List of items included in the invoice
    private List<InvoiceItem> items;

    // Total amount due for the invoice
    private double totalAmount;

    /////////////////////////
    //     Constructors    //
    /////////////////////////

    // Default constructor
    public Invoice() {}

    // Constructor to create an invoice with all fields
    public Invoice(int invoiceId, LocalDateTime invoiceDate, Customer customer, List<InvoiceItem> items, double totalAmount) 
    {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.customer = customer;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    /////////////////////////
    //     Getters         //
    /////////////////////////

    public int getInvoiceId() 
    {
        return invoiceId;
    }

    public LocalDateTime getInvoiceDate() 
    {
        return invoiceDate;
    }

    public Customer getCustomer() 
    {
        return customer;
    }

    public List<InvoiceItem> getItems() 
    {
        return items;
    }

    public double getTotalAmount() 
    {
        return totalAmount;
    }

    /////////////////////////
    // Setters             //
    /////////////////////////

    public void setInvoiceId(int invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) 
    {
        this.invoiceDate = invoiceDate;
    }

    public void setCustomer(Customer customer) 
    {
        this.customer = customer;
    }

    public void setItems(List<InvoiceItem> items) 
    {
        this.items = items;
    }

    public void setTotalAmount(double totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    /////////////////////////
    // Display Method      //
    /////////////////////////

    /**
     * Prints the full invoice details to the console,
     * including customer info, item list, and total amount.
     */
    public void displayInvoice() 
    {
        System.out.println("======================================");
        System.out.println("           INVOICE #" + invoiceId);
        System.out.println("======================================");
        System.out.println("Date: " + invoiceDate);
        System.out.println("\nCustomer:");

        // Print customer information
        customer.displayCustomerInfo();

        // Print each invoice item
        System.out.println("\nItems:");
        if (items != null) {
            for (InvoiceItem item : items) 
            {
                System.out.println(" - " + item);
            }
        }

        // Print the total
        System.out.printf("\nTOTAL AMOUNT: €%.2f%n", totalAmount);
        System.out.println("======================================\n");
    }
}
