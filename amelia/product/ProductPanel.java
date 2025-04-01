package amelia.product;

import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel 
{

    protected JTextField nameField = new JTextField();
    protected JComboBox<String> categoryCombo = new JComboBox<>(Product.getCategoryOptions());
    protected JTextField priceField = new JTextField();
    protected JTextField stockField = new JTextField();

    public ProductPanel() 
    {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Product Name:")); add(nameField);
        add(new JLabel("Category:")); add(categoryCombo);
        add(new JLabel("Price:")); add(priceField);
        add(new JLabel("Stock:")); add(stockField);
    }

    public Product getProductData() 
    {
        Product p = new Product();
        p.setName(nameField.getText());
        p.setCategory((String) categoryCombo.getSelectedItem());
        p.setPrice(Double.parseDouble(priceField.getText()));
        p.setStock(Integer.parseInt(stockField.getText()));
        return p;
    }

    public void setProductData(Product p) 
    {
        nameField.setText(p.getName());
        categoryCombo.setSelectedItem(p.getCategory());
        priceField.setText(String.valueOf(p.getPrice()));
        stockField.setText(String.valueOf(p.getStock()));
    }

    public void setEditable(boolean editable) 
    {
        nameField.setEditable(editable);
        categoryCombo.setEnabled(editable);
        priceField.setEditable(editable);
        stockField.setEditable(editable);
    }
}
