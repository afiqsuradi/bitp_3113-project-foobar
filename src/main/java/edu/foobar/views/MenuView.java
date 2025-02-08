package edu.foobar.views;

import edu.foobar.controllers.MenuController;
import edu.foobar.models.Enums;
import edu.foobar.models.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuView extends JFrame {
    private final Logger logger = LoggerFactory.getLogger(MenuView.class);
    private Map<Enums.FoodCategory, List<MenuItemPanel>> menuItemsByCategory = new HashMap<>();
    private JLabel availablePointsLabel;
    private JCheckBox redeemPointsCheckbox;
    private MenuController menuController;
    private JPanel receiptPanel;
    private JTextArea receiptTextArea;
    private JLabel totalLabel;
    private double orderTotal = 0.0;

    public MenuView() {
        menuController = new MenuController();

        setTitle("Menu Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Food Order");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel leftPanel = createMenuPanel();
        receiptPanel = createReceiptPanel();
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(receiptPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        List<Menu> menus = menuController.getAllMenus();

        for (Menu menu : menus) {
            Enums.FoodCategory category = menu.getCategory();
            if (!menuItemsByCategory.containsKey(category)) {
                menuItemsByCategory.put(category, new ArrayList<>());
            }
            menuItemsByCategory.get(category).add(new MenuItemPanel(menu, this::updateReceipt));
        }

        for (Enums.FoodCategory category : menuItemsByCategory.keySet()) {
            JLabel categoryLabel = new JLabel(category.toString());
            categoryLabel.setFont(new Font(categoryLabel.getFont().getName(), Font.BOLD, 16));
            categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            menuPanel.add(categoryLabel);
            menuPanel.add(Box.createVerticalStrut(5));

            for (MenuItemPanel itemPanel : menuItemsByCategory.get(category)) {
                itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                menuPanel.add(itemPanel);
                menuPanel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        containerPanel.setPreferredSize(new Dimension(600, 400));

        return containerPanel;
    }

    private JPanel createReceiptPanel() {
        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        receiptPanel.setPreferredSize(new Dimension(200, 400));

        receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: RM 0.00");
        totalLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        receiptPanel.add(totalLabel, BorderLayout.SOUTH);

        return receiptPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        availablePointsLabel = new JLabel("Available Point: XX");
        leftPanel.add(availablePointsLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        redeemPointsCheckbox = new JCheckBox("Redeem Point");
        redeemPointsCheckbox.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton proceedButton = new JButton("Proceed to payment");
        proceedButton.setBackground(Color.GRAY);
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(redeemPointsCheckbox);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(proceedButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private class MenuItemPanel extends JPanel {
        private JLabel quantityLabel;
        private Menu menu;
        private QuantityChangeListener quantityChangeListener;

        public MenuItemPanel(Menu menu, QuantityChangeListener listener) {
            this.menu = menu;
            this.quantityChangeListener = listener;
            setLayout(new GridBagLayout());
            setPreferredSize(new Dimension(550, 40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 5, 0, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            add(new JLabel(menu.getName()), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 0.5;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            pricePanel.add(new JLabel("RM" + menu.getFormattedPrice()));

            add(pricePanel, gbc);

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            gbc.anchor = GridBagConstraints.EAST;
            JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            JButton minusButton = new JButton("-");
            minusButton.setBackground(Color.GRAY);
            minusButton.setForeground(Color.WHITE);

            quantityLabel = new JLabel("0");
            quantityLabel.setPreferredSize(new Dimension(30,20));

            JButton plusButton = new JButton("+");
            plusButton.setBackground(Color.GRAY);
            plusButton.setForeground(Color.WHITE);
            quantityPanel.add(minusButton);
            quantityPanel.add(quantityLabel);
            quantityPanel.add(plusButton);

            add(quantityPanel, gbc);

            plusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int currentQuantity = Integer.parseInt(quantityLabel.getText());
                    int newQuantity = currentQuantity + 1;
                    quantityLabel.setText(String.valueOf(newQuantity));
                    quantityChangeListener.onQuantityChange(menu, newQuantity);
                }
            });

            minusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int currentQuantity = Integer.parseInt(quantityLabel.getText());
                    int newQuantity = currentQuantity > 0 ? currentQuantity - 1 : 0;
                    quantityLabel.setText(String.valueOf(newQuantity));
                    quantityChangeListener.onQuantityChange(menu, newQuantity);
                }
            });
        }

    }

    public static void showMenu() {
        SwingUtilities.invokeLater(() -> new MenuView());
    }

    private void updateReceipt(Menu menu, int quantity) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        double itemPrice = Double.parseDouble(menu.getFormattedPrice());
        double itemTotal = itemPrice * quantity;

        StringBuilder receiptText = new StringBuilder();
        orderTotal = 0.0;
        for (Map.Entry<Enums.FoodCategory, List<MenuItemPanel>> categoryEntry : menuItemsByCategory.entrySet()) {
            for (MenuItemPanel itemPanel : categoryEntry.getValue()) {
                String itemName = itemPanel.menu.getName();
                int itemQuantity = Integer.parseInt(itemPanel.quantityLabel.getText());

                if (itemQuantity > 0) {
                    double currentPrice = Double.parseDouble(itemPanel.menu.getFormattedPrice());
                    double currentItemTotal = currentPrice * itemQuantity;

                    receiptText.append(itemName).append(" x ").append(itemQuantity).append(" = ").append(formatter.format(currentItemTotal)).append("\n");
                    orderTotal += currentItemTotal;
                }
            }
        }
        receiptTextArea.setText(receiptText.toString());

        totalLabel.setText("Total: " + formatter.format(orderTotal));
    }

    private interface QuantityChangeListener {
        void onQuantityChange(Menu menu, int quantity);
    }
}