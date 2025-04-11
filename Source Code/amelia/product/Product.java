package amelia.product;

/**
 * Represents a product with basic details such as ID, name, category, price, and stock.
 * <p>
 * Includes validation to ensure price and stock are not negative.
 * Useful for managing product data in a store, inventory, or e-commerce system.
 * </p>
 */
public class Product 
{

    // Fields to store product details
    private int productId;
    private String name;
    private String category;
    private double price;
    private int stock;

    /**
     * Full constructor to create a Product with all fields.
     * Validation is applied to price and stock.
     *
     * @param productId The unique ID of the product
     * @param name      The name of the product
     * @param category  The category this product belongs to
     * @param price     The price of the product (must be non-negative)
     * @param stock     The quantity in stock (must be non-negative)
     * @throws IllegalArgumentException if price or stock is negative
     */
    public Product(int productId, String name, String category, double price, int stock) 
    {
        this.productId = productId;
        this.name = name;
        this.category = category;
        setPrice(price);  // Use setter to apply validation
        setStock(stock);  // Use setter to apply validation
    }

    /**
     * Empty constructor, often used when creating a product and filling fields later.
     */
    public Product() {}

    /////////////
    // Getters //
    /////////////

    /**
     * @return the product ID
     */
    public int getProductId() 
    {
        return productId;
    }

    /**
     * @return the product name
     */
    public String getName() 
    {
        return name;
    }

    /**
     * @return the category the product belongs to
     */
    public String getCategory() 
    {
        return category;
    }

    /**
     * @return the product's price
     */
    public double getPrice() 
    {
        return price;
    }

    /**
     * @return the amount of stock available
     */
    public int getStock() 
    {
        return stock;
    }

    /////////////
    // Setters //
    /////////////

    /**
     * Sets the product ID.
     *
     * @param productId the new product ID
     */
    public void setProductId(int productId) 
    {
        this.productId = productId;
    }

    /**
     * Sets the product name.
     *
     * @param name the new name of the product
     */
    public void setName(String name) 
    {
        this.name = name;
    }

    /**
     * Sets the product category.
     *
     * @param category the new category of the product
     */
    public void setCategory(String category) 
    {
        this.category = category;
    }

    /**
     * Sets the price of the product.
     * Only allows non-negative values.
     *
     * @param price the new price
     * @throws IllegalArgumentException if price is negative
     */
    public void setPrice(double price) 
    {
        if (price >= 0) 
        {
            this.price = price;
        } 
        else 
        {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    /**
     * Sets the stock quantity of the product.
     * Only allows non-negative values.
     *
     * @param stock the new stock amount
     * @throws IllegalArgumentException if stock is negative
     */
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

    /**
     * Prints all product details to the console.
     * Useful for debugging or testing.
     */
    public void displayProductInfo() 
    {
        System.out.println("Product ID: " + productId);
        System.out.println("Product Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: $" + price);
        System.out.println("Stock Quantity: " + stock);
    }

    /**
     * Returns a list of category options.
     * Typically used in dropdowns in the GUI.
     *
     * @return an array of available product categories
     */
    public static String[] getCategoryOptions() 
    {
        return new String[] 
        {
            "Electronics", "Clothing", "Books", "Home", "Sports", "Food", "Other"
        };
    }

    /**
     * Returns a simple string representation of the product.
     *
     * @return formatted string like "Phone (Electronics) - €599.99"
     */
    @Override
    public String toString() 
    {
        return name + " (" + category + ") - €" + String.format("%.2f", price);
    }
}
