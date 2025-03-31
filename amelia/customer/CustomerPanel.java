// package: amelia.customer
package amelia.customer;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    protected JTextField fnameField = new JTextField();
    protected JTextField snameField = new JTextField();
    protected JTextField addressField = new JTextField();
    protected JTextField emailField = new JTextField();
    protected JTextField phoneField = new JTextField();

    public CustomerPanel() 
    {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("First Name:")); add(fnameField);
        add(new JLabel("Surname:")); add(snameField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Email:")); add(emailField);
        add(new JLabel("Phone:")); add(phoneField);
    }

    public Customer getCustomerData() {
        Customer customer = new Customer();
        customer.setFname(fnameField.getText());
        customer.setSname(snameField.getText());
        customer.setAddress(addressField.getText());
        customer.setEmail(emailField.getText());
        customer.setPhone(phoneField.getText());
        return customer;
    }

    public void setCustomerData(Customer c) {
        fnameField.setText(c.getFname());
        snameField.setText(c.getSname());
        addressField.setText(c.getAddress());
        emailField.setText(c.getEmail());
        phoneField.setText(c.getPhone());
    }

    public void setEditable(boolean editable) {
        fnameField.setEditable(editable);
        snameField.setEditable(editable);
        addressField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
    }
}
