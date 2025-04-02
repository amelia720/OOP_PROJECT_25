package amelia.invoice;

public class InvoiceItem {
    private int productId;
    private String productName;
    private String category;
    private double unitPrice;
    private int quantity;
    private double totalAmount;

    // Default constructor
    public InvoiceItem() {}

    // Full constructor
    public InvoiceItem(int productId, String productName, String category,
                       double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalAmount = unitPrice * quantity;
    }

    // Getters
    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        updateTotal();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotal();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private void updateTotal() {
        this.totalAmount = this.unitPrice * this.quantity;
    }

    // Display
    @Override
    public String toString() {
        return String.format("%s (%s) x%d @ €%.2f = €%.2f",
            productName, category, quantity, unitPrice, totalAmount);
    }
}
