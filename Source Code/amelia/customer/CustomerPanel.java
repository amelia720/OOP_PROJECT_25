package amelia.customer;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

/**
 * This class is a custom JPanel for handling customer input.
 * It includes text fields for first name, surname, address, email, and phone number.
 * I used GridLayout to organize the labels and fields in a clean way.
 */
public class CustomerPanel extends JPanel 
{

    // These JTextFields hold user input for each customer detail
    protected JTextField fnameField = new JTextField();
    protected JTextField snameField = new JTextField();
    protected JTextField addressField = new JTextField();
    protected JTextField emailField = new JTextField();
    protected JTextField phoneField = new JTextField();

    /**
     * Sets up the form where users can enter customer details.
     * Uses a grid layout, which places components in rows and columns
     * like a table — each label is placed next to its matching text field.
     * Adds space between them and padding around the edges to make it look neat.
     */
    public CustomerPanel() 
    {
        // I used a 6-row, 2-column GridLayout with spacing between components.
        // hgap (space between comlums) and vgap (between rows) are set to 10
        setLayout(new GridLayout(6, 2, 10, 10));
        // Creates an empty border with 20px padding on all sides
        // This makes sure the components inside the panel aren't touching the edges
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // Add each label with its text field side by side
        add(new JLabel("First Name:")); add(fnameField);
        add(new JLabel("Surname:")); add(snameField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Phone:")); add(phoneField);

    }

    /**
     * Gets the data entered into the form and puts it into a new Customer object.
     * This method reads the text from each field and sets it using the Customer's setters.
     *
     * From the form to the Customer Object
     * 
     * @return A Customer object filled with the user's input.
     */
    public Customer getCustomerData() 
    {
        //create new customer object via the default constructor
        Customer customer = new Customer();

        // Fill the customer object with input from the form using setter methods
        customer.setFname(fnameField.getText());
        customer.setSname(snameField.getText());
        customer.setAddress(addressField.getText());
        customer.setEmail(emailField.getText());
        customer.setPhone(phoneField.getText());


        // Return the Customer object created in getCustomerData() so the calling code can use it
        return customer;

    }

    /**
     * Loads customer data into the form fields.
     * Takes a Customer object and sets each text field to show its values.
     *
     * From the form to the Customer Object
     * 
     * @param c The Customer whose information will be displayed in the form.
     */
    public void setCustomerData(Customer c) 
    {
        // Set each text field with the corresponding value from the Customer object
        fnameField.setText(c.getFname());
        snameField.setText(c.getSname());
        addressField.setText(c.getAddress());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getPhone());
    }


    /**
     * This method lets me turn the form on or off for editing.
     * It's useful if I want to display the customer info but not allow changes.
     * 
     * @param editable true = fields can be changed, false = read-only
     */
    public void setEditable(boolean editable) 
    {
        fnameField.setEditable(editable);
        snameField.setEditable(editable);
        addressField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
    }
}
