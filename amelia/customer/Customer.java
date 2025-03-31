package amelia.customer;

public class Customer 
{
    private int customerId; // Unique ID for each customer
    private String fname;
    private String sname;
    private String address;
    private String email;
    private String phone;

    // Constructor with required attributes
    /**
     * 
     * @param customerId - Customer Id
     * @param fname - First Name
     * @param sname - Surname
     * @param address - Address
     * @param email - Email
     * @param phone - Phone
     */
    public Customer(int customerId, String fname, String sname, String address, String email, String phone) 
    {
        this.customerId = customerId;
        this.fname = fname;
        this.sname = sname;
        this.address = address;
        setEmail(email);  // Using setter for validation
        setPhone(phone);  // Using setter for validation
    }

    // Default constructor
    public Customer() {}

    // Getters
    public int getCustomerId() {
        return customerId;
    }

    public String getFname() {
        return fname;
    }

    public String getSname() {
        return sname;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param customerId - Customer Id to Set
     */
    // Setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * 
     * @param fname - First Name to Set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 
     * @param sname - suraname to set
     */
    public void setSname(String sname) {
        this.sname = sname;
    }
    /**
     * 
     * @param address - name to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the email address after validating the format.
     * @param email - email validation
     */
    public void setEmail(String email) 
    {
        if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) 
        {
            // Enhanced email validation
            this.email = email;
        } 
        else 
        {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    /**
     * Sets the phone number after validating it is 10 digits.
     * @param phone - Phone Validation
     */
    public void setPhone(String phone) 
    {
        if (phone.matches("\\d{10}")) 
        {
             // 10-digit number validation
            this.phone = phone;
        } 
        else {

            throw new IllegalArgumentException("Invalid phone number format. Must be 10 digits.");
        }
    }

    // Display customer details
    /**
     * Displays all customer details in a formatted output.
     */
    public void displayCustomerInfo() 
    {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + fname + " " + sname);
        System.out.println("Address: " + address);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
    }
}
