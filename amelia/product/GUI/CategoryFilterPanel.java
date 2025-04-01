package amelia.product.GUI;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

import amelia.product.Product;

public class CategoryFilterPanel extends JPanel 
{

    private JComboBox<String> categoryCombo;
    private JButton filterButton;

    public CategoryFilterPanel(Consumer<String> onFilter) 
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("All Categories");
        for (String category : Product.getCategoryOptions()) 
        {
            categoryCombo.addItem(category);
        }

        filterButton = new JButton("Filter");
        filterButton.addActionListener(e -> 
        {
            String selected = (String) categoryCombo.getSelectedItem();
            onFilter.accept(selected);
        });

        add(new JLabel("Filter by Category:"));
        add(categoryCombo);
        add(filterButton);
    }

    public void resetSelection() 
    {
        categoryCombo.setSelectedIndex(0);
    }
}
