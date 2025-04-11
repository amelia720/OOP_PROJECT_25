package amelia.customer;

/**
 * This class represents a customer in the system.
 * It stores their name, contact details, and a unique ID.
 */
public class Customer 
{
    private int customerId; // Unique ID for each customer
    private String fname;
    private String sname;
    private String address;
    private String email;
    private String phone;

    /**
     * This constructor is used when I want to create a customer with all their details straight away.
     *
     * @param customerId the customer's unique ID
     * @param fname the customer's first name
     * @param sname the customer's surname
     * @param address the customer's home address
     * @param email the customer's email address (validated)
     * @param phone the customer's phone number (validated)
     */
    public Customer(int customerId, String fname, String sname, String address, String email, String phone) 
    {
        this.customerId = customerId;
        this.fname = fname;
        this.sname = sname;
        this.address = address;
        setEmail(email);  // Use setter so it gets validated
        setPhone(phone);  // Use setter so it gets validated
    }

    /**
     * This is the default constructor (in case I want to set the values later).
     */
    public Customer() {}

    // ======= Getters =======

    /**
     * @return the customer's ID
     */
    public int getCustomerId() 
    {
        return customerId;
    }

    /**
     * @return the customer's first name
     */
    public String getFname() 
    {
        return fname;
    }

    /**
     * @return the customer's surname
     */
    public String getSname() 
    {
        return sname;
    }

    /**
     * @return the customer's address
     */
    public String getAddress() 
    {
        return address;
    }

    /**
     * @return the customer's email
     */
    public String getEmail() 
    {
        return email;
    }

    /**
     * @return the customer's phone number
     */
    public String getPhone() 
    {
        return phone;
    }

    // ======= Setters =======

    /**
     * @param customerId sets the customer's ID
     */
    public void setCustomerId(int customerId) 
    {
        this.customerId = customerId;
    }

    /**
     * @param fname sets the customer's first name
     */
    public void setFname(String fname) 
    {
        this.fname = fname;
    }

    /**
     * @param sname sets the customer's surname
     */
    public void setSname(String sname) 
    {
        this.sname = sname;
    }

    /**
     * @param address sets the customer's address
     */
    public void setAddress(String address) 
    {
        this.address = address;
    }

    /**
     * Sets the customer's email, but also checks if it's a valid format.
     *
     * @param email the customer's email to set
     * @throws IllegalArgumentException if the email is not in the right format
     */
    public void setEmail(String email) 
    {
        if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) 
        {
            this.email = email;
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    /**
     * Sets the customer's phone number, but makes sure it's exactly 10 digits.
     *
     * @param phone the customer's phone number
     * @throws IllegalArgumentException if the phone number isn't 10 digits
     */
    public void setPhone(String phone) 
    {
        if (phone.matches("\\d{10}")) 
        {
            this.phone = phone;
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits.");
        }
    }

    /**
     * Prints out all the customer’s details in the console.
     */
    public void displayCustomerInfo() 
    {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + fname + " " + sname);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }

    /**
     * Returns a simple string showing the customer's name and email.
     *
     * @return formatted string for displaying customer
     */
    @Override
    public String toString() 
    {
        return fname + " " + sname + " (" + email + ")";
    }
}
