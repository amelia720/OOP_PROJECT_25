package amelia.product;

public class Product 
{
    // Fields to store product details
    private int productId;
    private String name;
    private String category;
    private double price;
    private int stock;

    // Full constructor — used when we know all the product details
    public Product(int productId, String name, String category, double price, int stock) 
    {
        this.productId = productId;
        this.name = name;
        this.category = category;
        setPrice(price);  // Use setter to apply validation
        setStock(stock);  // Use setter to apply validation
    }

    // Empty constructor — useful when creating a product before filling its fields
    public Product() {}

    ///////////////////
    // Getter methods
    ///////////////////

    public int getProductId() 
    {
        return productId;
    }

    public String getName() 
    {
        return name;
    }

    public String getCategory() 
    {
        return category;
    }

    public double getPrice() 
    {
        return price;
    }

    public int getStock() 
    {
        return stock;
    }

    ///////////////////
    // Setter methods
    ///////////////////

    public void setProductId(int productId) 
    {
        this.productId = productId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    // Only allow non-negative prices
    public void setPrice(double price) 
    {
        if (price >= 0) {
            this.price = price;
        } 
        else 
        {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    // Only allow non-negative stock
    public void setStock(int stock) 
    {
        if (stock >= 0) 
        {
            this.stock = stock;
        } 
        else 
        {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }

    /////////////////////////////////////
    // Display method for CLI debugging
    /////////////////////////////////////

    public void displayProductInfo() 
    {
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: $" + price);
        System.out.println("Stock Quantity: " + stock);
    }

    /////////////////////////////////////
    // Returns category options for GUI
    /////////////////////////////////////
    public static String[] getCategoryOptions() 
    {
        return new String[] 
        {
            "Electronics", "Clothing", "Books", "Home", "Sports", "Food", "Other"
        };
    }

    ///////////////////////////////
    // Show product as a string
    ///////////////////////////////
    @Override
    public String toString() {
        return name + " (" + category + ") - €" + String.format("%.2f", price);
    }
}
