package edu.foobar.views;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.foobar.controllers.MembershipController;
import edu.foobar.dao.MembershipDao;
import edu.foobar.models.Membership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginView extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
    private static final int DEFAULT_MEMBERSHIP_ID = 1;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel userPanel;
    private JTextField emailField;
    private MembershipController membershipController;

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private final Color PRIMARY_COLOR = new Color(0x3F51B5);
    private final Color SECONDARY_COLOR = new Color(0x673AB7);
    private final Color BACKGROUND_COLOR = new Color(0xE8EAF6);
    private final Color TEXT_PRIMARY_COLOR = new Color(0x212121);
    private final Color TEXT_SECONDARY_COLOR = new Color(0x757575);
    private final Color ACCENT_COLOR = new Color(0xFF4081);


    public LoginView() {
        this.membershipController = new MembershipController(new MembershipDao());
        setTitle("Food Ordering System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "login");

        userPanel = createUserPanel();
        mainPanel.add(userPanel, "user");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("FOOD ORDERING SYSTEM");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton adminButton = new JButton("ADMIN");
        JButton userButton = new JButton("USER");

        styleButton(adminButton);
        styleButton(userButton);

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);

        adminButton.addActionListener(e -> {
            new AdminView();
            dispose();
        });

        userButton.addActionListener(e -> cardLayout.show(mainPanel, "user"));

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("FOOD ORDERING SYSTEM");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int buttonWidth = 240;
        JLabel emailLabel = new JLabel("Enter email:");
        emailLabel.setForeground(TEXT_SECONDARY_COLOR);

        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(buttonWidth, 30));
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(TEXT_PRIMARY_COLOR);

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new GridLayout(2, 1));
        emailPanel.setBackground(BACKGROUND_COLOR);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        emailPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel emailPanelWrapper = new JPanel();
        emailPanelWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        emailPanelWrapper.setBackground(BACKGROUND_COLOR);
        emailPanelWrapper.add(emailPanel);
        emailPanelWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = new JButton("BACK");
        JButton orderNowButton = new JButton("ORDER NOW");

        styleButton(backButton);
        styleButton(orderNowButton);

        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        orderNowButton.setFont(new Font("Arial", Font.BOLD, 12));

        buttonPanel.add(backButton);
        buttonPanel.add(orderNowButton);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        orderNowButton.addActionListener(e -> {
            String email = emailField.getText();
            if (isValidEmail(email)) {
                Membership membership = membershipController.getOrCreateMembership(email);
                if (membership != null) {
                    MenuView.showMenu(membership);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid email address.",
                        "Invalid Email",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(emailPanelWrapper);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginView::new);
    }
}