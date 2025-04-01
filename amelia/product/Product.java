package amelia.product;

public class Product 
{
    private int productId;
    private String name;
    private String category;
    private double price;
    private int stock;

    // Constructor
    public Product(int productId, String name, String category, double price, int stock) 
    {
        this.productId = productId;
        this.name = name;
        this.category = category;
        setPrice(price);  // Using setter for validation
        setStock(stock);  // Using setter for validation
    }

    public Product() {}

    // Getters
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

    // Setters
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

    // Display Product Details
    public void displayProductInfo() 
    {
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: $" + price);
        System.out.println("Stock Quantity: " + stock);
    }
    public static String[] getCategoryOptions() 
    {
        return new String[] 
        {
            "Electronics", "Clothing", "Books", "Home", "Sports", "Food", "Other"
        };
    }
}
