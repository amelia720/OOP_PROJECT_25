package amelia.product.GUI;

// GUI imports
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;

import java.util.function.Consumer;

import amelia.product.Product;

/**
 * A small panel with a dropdown and a button to filter products by category.
 */
public class CategoryFilterPanel extends JPanel 
{
    // Dropdown list for selecting category
    private JComboBox<String> categoryCombo;

    // Button to trigger the filtering
    private JButton filterButton;

    /**
     * Constructor takes a function (Consumer) that defines what happens when the filter button is clicked.
     */
    public CategoryFilterPanel(Consumer<String> onFilter) 
    {
        // Layout arranges components left to right
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Create dropdown and add the "All" option first
        categoryCombo = new JComboBox<>();
        categoryCombo.addItem("All Categories");

        // Add all available categories from the Product class
        for (String category : Product.getCategoryOptions()) 
        {
            categoryCombo.addItem(category);
        }

        // Create the "Filter" button
        filterButton = new JButton("Filter");

        // When the button is clicked, pass the selected category to the provided onFilter function
        filterButton.addActionListener(e -> 
        {
            String selected = (String) categoryCombo.getSelectedItem();
            onFilter.accept(selected); // call the external filter function
        });

        // Add all components to the panel
        add(new JLabel("Filter by Category:")); // Label next to dropdown
        add(categoryCombo);                     // Dropdown
        add(filterButton);                      // Button
    }

    /**
     * Resets the category selection to the default ("All Categories").
     */
    public void resetSelection() 
    {
        categoryCombo.setSelectedIndex(0); // 0 = first item
    }
}
