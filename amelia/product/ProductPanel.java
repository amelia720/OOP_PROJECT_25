package amelia.product;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

/**
 * GUI panel for inputting and displaying product data.
 * Contains fields for name, category, price, and stock.
 */
public class ProductPanel extends JPanel 
{
    // Input fields for product data
    protected JTextField nameField = new JTextField();
    protected JComboBox<String> categoryCombo = new JComboBox<>(Product.getCategoryOptions());
    protected JTextField priceField = new JTextField();
    protected JTextField stockField = new JTextField();

    /**
     * Constructor to set up the layout and add all form components.
     */
    public ProductPanel() 
    {
        // Use a grid layout: 5 rows, 2 columns, with spacing
        setLayout(new GridLayout(5, 2, 10, 10));
        // Add padding around the panel
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add labels and input fields side by side
        add(new JLabel("Product Name:")); add(nameField);
        add(new JLabel("Category:"));     add(categoryCombo);
        add(new JLabel("Price:"));        add(priceField);
        add(new JLabel("Stock:"));        add(stockField);
    }

    /**
     * Creates a Product object from the current form inputs.
     * @return A Product filled with user input data.
     */
    public Product getProductData() 
    {
        Product p = new Product();
        p.setName(nameField.getText());
        p.setCategory((String) categoryCombo.getSelectedItem());
        p.setPrice(Double.parseDouble(priceField.getText()));
        p.setStock(Integer.parseInt(stockField.getText()));
        return p;
    }

    /**
     * Populates the form fields using a Product object.
     * @param p The Product whose data will be shown in the form.
     */
    public void setProductData(Product p) 
    {
        nameField.setText(p.getName());
        categoryCombo.setSelectedItem(p.getCategory());
        priceField.setText(String.valueOf(p.getPrice()));
        stockField.setText(String.valueOf(p.getStock()));
    }

    /**
     * Enables or disables the fields for editing.
     * Useful when showing product data in read-only mode.
     * @param editable true = fields can be edited, false = read-only
     */
    public void setEditable(boolean editable) 
    {
        nameField.setEditable(editable);
        categoryCombo.setEnabled(editable);
        priceField.setEditable(editable);
        stockField.setEditable(editable);
    }
}
