package amelia.invoice;
import java.util.Date;

import amelia.customer.Customer;

class Invoice 
{
    private int invoiceId;
    private Customer customer;
    private Date invoiceDate;
    private double totalAmount;

    // Constructor - Automatically sets today's date if no date is provided
    public Invoice(int invoiceId, Customer customer, double totalAmount) 
    {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.invoiceDate = new Date(); // Auto-sets today's date
        this.totalAmount = totalAmount;
    }

    // Getters
    public int getInvoiceId() 
    {
        return invoiceId;
    }

    public Customer getCustomer() 
    {
        return customer;
    }

    public Date getInvoiceDate() 
    {
        return invoiceDate;
    }

    public double getTotalAmount() 
    {
        return totalAmount;
    }

    // Setters
    public void setInvoiceId(int invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public void setCustomer(Customer customer) 
    {
        this.customer = customer;
    }

    public void setInvoiceDate(Date invoiceDate) 
    {
        this.invoiceDate = invoiceDate;
    }

    public void setTotalAmount(double totalAmount) 
    {
        if (totalAmount >= 0) {
            this.totalAmount = totalAmount;
        } else {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
    }

    // Display Invoice Details
    public void displayInvoice() 
    {
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Customer: " + customer.getFname() + " " + customer.getSname());
        System.out.println("Invoice Date: " + invoiceDate);
        System.out.println("Total Amount: $" + totalAmount);
    }
}
