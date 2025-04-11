package amelia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import amelia.customer.GUI.AddCustomerPanel;
import amelia.customer.GUI.AmendViewCustomerPanel;
import amelia.customer.GUI.DeleteCustomerPanel;
import amelia.product.GUI.AddProductPanel;
import amelia.product.GUI.AmendViewProductPanel;
import amelia.product.GUI.DeleteProductPanel;
import amelia.invoice.GUI.AddInvoicePanel;
import amelia.invoice.GUI.AmendViewInvoicePanel;
import amelia.invoice.GUI.DeleteInvoicePanel;


/**
 * Main window for the ShopManager Pro application.
 * <p>
 * This frame includes a top bar, a sidebar with navigation buttons,
 * and a main content area where different panels (like Add Product, Edit Customer) are shown.
 * </p>
 */
public class MainFrame extends JFrame 
{

    // List to keep track of all navigation buttons for styling
    private final ArrayList<JButton> allButtons = new ArrayList<>();

    // Tracks the currently selected main and sub navigation buttons
    private JButton currentMainButton = null;
    private JButton currentSubButton = null;

    // Displays project and student info on the home screen
    private final HomePage projectInfo = new HomePage();

    /**
     * Constructor that sets up the entire main UI frame,
     * including the sidebar, top bar, and default home screen.
     */
    public MainFrame() 
    {
        setTitle("ShopManager Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        //////////////////////
        // Top bar creation //
        //////////////////////
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 33, 33));
        topPanel.setPreferredSize(new Dimension(getWidth(), 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("ShopManager Pro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.WEST);

        ///////////////////////////
        // Sidebar for nav menu  //
        ///////////////////////////
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(245, 245, 245));
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        //////////////////////////////
        // Main content display area //
        //////////////////////////////
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //////////////////////
        // Home page button //
        //////////////////////
        JButton homeButton = createNavButton("Home");
        homeButton.addActionListener(e -> 
        {
            currentMainButton = homeButton;
            currentSubButton = null;
            updateHighlighting(); // highlight selected button

            contentPanel.removeAll(); // clear current panel

            // Create home panel with title and student info
            JLabel homeTitle = new JLabel("Home");
            homeTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            homeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            JTextArea homeText = new JTextArea(
                projectInfo.getDescription() + "\n\nPresented by:\n" +
                "Name: " + projectInfo.getStudentName() + "\n" +
                "Student Number: " + projectInfo.getStudentNumber() + "\n" +
                "Course Code: " + projectInfo.getCourseCode()
            );
            homeText.setEditable(false);
            homeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            homeText.setLineWrap(true);
            homeText.setWrapStyleWord(true);
            homeText.setBackground(Color.WHITE);
            homeText.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            JPanel homePanel = new JPanel(new BorderLayout());
            homePanel.setBackground(Color.WHITE);
            homePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            homePanel.add(homeTitle, BorderLayout.NORTH);
            homePanel.add(homeText, BorderLayout.CENTER);

            contentPanel.add(homePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        sidePanel.add(homeButton);
        sidePanel.add(Box.createVerticalStrut(10));

        /////////////////////////////////////
        // Add dropdown sections to sidebar //
        /////////////////////////////////////
        sidePanel.add(createDropdown("Customer", new String[]{"Add Customer", "Edit Customer", "Delete Customer"}, contentPanel));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(createDropdown("Product", new String[]{"Add Product", "Edit Product", "Delete Product"}, contentPanel));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(createDropdown("Invoice", new String[]{"Add Invoice", "Edit Invoice", "Delete Invoice"}, contentPanel));

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Show home panel by default
        homeButton.doClick();
        setVisible(true);
    }

    /**
     * Creates a dropdown section in the sidebar for a category (e.g. Customer).
     *
     * @param title         The title of the section (main button)
     * @param options       The list of sub-options (sub-buttons)
     * @param contentPanel  The panel where the selected screen will be shown
     * @return A JPanel containing the dropdown section
     */
    private JPanel createDropdown(String title, String[] options, JPanel contentPanel) 
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        // Main (expand/collapse) button
        JButton mainButton = createNavButton(title);
        panel.add(mainButton);
        panel.add(Box.createVerticalStrut(5));

        // Panel to hold the sub-buttons (hidden by default)
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(245, 245, 245));
        subPanel.setVisible(false);

        // Toggle sub-panel visibility on main button click
        mainButton.addActionListener(e -> 
        {
            subPanel.setVisible(!subPanel.isVisible());
            panel.revalidate();
        });

        // Create each sub-option button
        for (String option : options) 
        {
            JButton subButton = createSubOptionButton(option, contentPanel, mainButton);
            subPanel.add(subButton);
        }

        panel.add(subPanel);
        return panel;
    }

    /**
     * Creates a main navigation button (e.g. Home, Customer, Product).
     *
     * @param text The text to display on the button
     * @return A styled JButton
     */
    private JButton createNavButton(String text) 
    {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.setMaximumSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        allButtons.add(button);
        return button;
    }

    /**
     * Creates a sub-option button inside a dropdown (e.g. "Add Customer").
     *
     * @param label         The button label
     * @param contentPanel  The panel where selected panel will be shown
     * @param parentButton  The main button this sub belongs to
     * @return A styled sub-button
     */
    private JButton createSubOptionButton(String label, JPanel contentPanel, JButton parentButton) 
    {
        JButton button = new JButton(label);
        button.setFocusPainted(false);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 35));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY),
            BorderFactory.createEmptyBorder(5, 30, 5, 5)));

        allButtons.add(button);

        // Define action when sub-option is clicked
        button.addActionListener(e -> 
        {
            currentMainButton = parentButton;
            currentSubButton = button;
            updateHighlighting(); // highlight selected

            contentPanel.removeAll();
            JPanel selectedPanel;

            // Load the correct panel based on button label
            switch (label.toLowerCase()) 
            {
                case "add customer": selectedPanel = new AddCustomerPanel(); break;
                case "delete customer": selectedPanel = new DeleteCustomerPanel(); break;
                case "edit customer": selectedPanel = new AmendViewCustomerPanel(); break;
                case "add product": selectedPanel = new AddProductPanel(); break;
                case "delete product": selectedPanel = new DeleteProductPanel(); break;
                case "edit product": selectedPanel = new AmendViewProductPanel(); break;
                case "add invoice": selectedPanel = new AddInvoicePanel(); break;
                case "delete invoice": selectedPanel = new DeleteInvoicePanel(); break;
                case "edit invoice": selectedPanel = new AmendViewInvoicePanel(); break;
                default:
                    selectedPanel = new JPanel();
                    selectedPanel.add(new JLabel("You selected: " + label));
                    break;
            }

            // Add title above the panel
            JPanel wrappedPanel = new JPanel(new BorderLayout());
            JLabel titleLabel = new JLabel(label);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            wrappedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            wrappedPanel.add(titleLabel, BorderLayout.NORTH);
            wrappedPanel.add(selectedPanel, BorderLayout.CENTER);

            contentPanel.add(wrappedPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        return button;
    }

    /**
     * Updates the background color of all buttons based on current selection.
     * Main and sub buttons are styled differently to show which is active.
     */
    private void updateHighlighting() 
    {
        for (JButton btn : allButtons) 
        {
            boolean isMain = btn.getFont().isBold();
            if (btn == currentMainButton || btn == currentSubButton) 
            {
                btn.setBackground(Color.BLACK); // highlight selected
            } 
            else 
            {
                btn.setBackground(isMain ? new Color(80, 80, 80) : Color.GRAY);
            }
            btn.setForeground(Color.WHITE);
        }
    }
}
