package amelia;

import javax.swing.*;
import amelia.customer.GUI.AddCustomerPanel;
import amelia.customer.GUI.DeleteCustomerPanel;
import amelia.customer.GUI.AmendViewCustomerPanel;
import amelia.product.GUI.AddProductPanel;
import amelia.product.GUI.AmendViewProductPanel;
import amelia.product.GUI.DeleteProductPanel;
import amelia.invoice.GUI.AddInvoicePanel;
import amelia.invoice.GUI.AmendViewInvoicePanel;
import amelia.invoice.GUI.DeleteInvoicePanel;

import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private final ArrayList<JButton> allButtons = new ArrayList<>();
    private JButton currentMainButton = null;
    private JButton currentSubButton = null;
    private final HomePage projectInfo = new HomePage();

    public MainFrame() {
        setTitle("ShopManager Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(33, 33, 33));
        topPanel.setPreferredSize(new Dimension(getWidth(), 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("ShopManager Pro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(title, BorderLayout.WEST);

        // Sidebar
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(245, 245, 245));
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Home button
        JButton homeButton = createNavButton("Home");
        homeButton.addActionListener(e -> {
            currentMainButton = homeButton;
            currentSubButton = null;
            updateHighlighting();

            contentPanel.removeAll();

            // Home title
            JLabel homeTitle = new JLabel("Home");
            homeTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            homeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            // Description and student info
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
            homeText.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            homeText.setBackground(Color.WHITE);

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

        // Dropdowns
        sidePanel.add(createDropdown("Customer", new String[]{"Add Customer", "Edit Customer", "Delete Customer"}, contentPanel));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(createDropdown("Product", new String[]{"Add Product", "Edit Product", "Delete Product"}, contentPanel));
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(createDropdown("Invoice", new String[]{"Add Invoice", "Edit Invoice", "Delete Invoice"}, contentPanel));

        add(topPanel, BorderLayout.NORTH);
        add(sidePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Load Home screen initially
        homeButton.doClick();
        setVisible(true);
    }

    private JPanel createDropdown(String title, String[] options, JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));

        JButton mainButton = createNavButton(title);
        panel.add(mainButton);
        panel.add(Box.createVerticalStrut(5));

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(new Color(245, 245, 245));
        subPanel.setVisible(false);

        mainButton.addActionListener(e -> {
            subPanel.setVisible(!subPanel.isVisible());
            panel.revalidate();
        });

        for (String option : options) {
            JButton subButton = createSubOptionButton(option, contentPanel, mainButton);
            subPanel.add(subButton);
        }

        panel.add(subPanel);
        return panel;
    }

    private JButton createNavButton(String text) {
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

    private JButton createSubOptionButton(String label, JPanel contentPanel, JButton parentButton) {
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

        button.addActionListener(e -> {
            currentMainButton = parentButton;
            currentSubButton = button;
            updateHighlighting();

            contentPanel.removeAll();
            JPanel selectedPanel;

            switch (label.toLowerCase()) {
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

    private void updateHighlighting() {
        for (JButton btn : allButtons) {
            boolean isMain = btn.getFont().isBold();
            if (btn == currentMainButton || btn == currentSubButton) {
                btn.setBackground(Color.BLACK);
            } else {
                btn.setBackground(isMain ? new Color(80, 80, 80) : Color.GRAY);
            }
            btn.setForeground(Color.WHITE);
        }
    }
}
