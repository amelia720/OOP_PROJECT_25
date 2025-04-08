package amelia.invoice;

/**
 * Represents a single item in an invoice.
 * Includes product info, quantity, unit price, and total amount.
 */
public class InvoiceItem 
{
    // Fields to store product and invoice item info
    private int productId;
    private String productName;
    private String category;
    private double unitPrice;
    private int quantity;
    private double totalAmount;

    // Default constructor (needed for creating empty objects)
    public InvoiceItem() {}

    // Full constructor to initialize all fields and calculate total
    public InvoiceItem(int productId, String productName, String category,
                       double unitPrice, int quantity) 
    {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalAmount = unitPrice * quantity;
    }

    /////////////////////////
    // Getters (accessors) //
    /////////////////////////

    public int getProductId() 
    {
        return productId;
    }

    public String getProductName() 
    {
        return productName;
    }

    public String getCategory() 
    {
        return category;
    }

    public double getUnitPrice() 
    {
        return unitPrice;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    public double getTotalAmount() 
    {
        return totalAmount;
    }

    /////////////////////////
    // Setters (mutators)  //
    /////////////////////////

    public void setProductId(int productId) 
    {
        this.productId = productId;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    // Updates unit price and recalculates total
    public void setUnitPrice(double unitPrice) 
    {
        this.unitPrice = unitPrice;
        updateTotal();
    }

    // Updates quantity and recalculates total
    public void setQuantity(int quantity) 
    {
        this.quantity = quantity;
        updateTotal();
    }

    // Directly sets the total amount (used when loading from DB)
    public void setTotalAmount(double totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    // Helper method to recalculate totalAmount when price or quantity changes
    private void updateTotal() 
    {
        this.totalAmount = this.unitPrice * this.quantity;
    }

    /////////////////////
    // Display Format  //
    /////////////////////

    // Returns a string like: "Laptop (Electronics) x2 @ €499.99 = €999.98"
    // %s is string, %d is integer, %.2f is float with 2 decimal places
    @Override
    public String toString() 
    {
        return String.format("%s (%s) x%d @ €%.2f = €%.2f",
            productName, category, quantity, unitPrice, totalAmount);
    }
}
