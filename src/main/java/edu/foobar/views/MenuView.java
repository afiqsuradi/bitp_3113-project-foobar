package edu.foobar.views;
import edu.foobar.controllers.MenuController;
import edu.foobar.controllers.OrderController;
import edu.foobar.controllers.PaymentController;
import edu.foobar.models.Enums;
import edu.foobar.models.Membership;
import edu.foobar.models.Menu;
import edu.foobar.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;

public class MenuView extends JFrame {

    private final Logger logger = LoggerFactory.getLogger(MenuView.class);
    private final Membership membership;
    private final Map<Enums.FoodCategory, List<MenuItemPanel>> menuItemsByCategory = new HashMap<>();
    private JLabel availablePointsLabel;
    private JCheckBox redeemPointsCheckbox;
    private MenuController menuController;
    private JPanel receiptPanel;
    private JTextArea receiptTextArea;
    private JLabel totalLabel;
    private double orderTotal = 0.0;
    private OrderController orderController;
    private List<OrderItem> orderItems = new ArrayList<>();
    private JButton proceedButton; // NEW
    private PaymentController paymentController; //NEW

    public MenuView(Membership membership) {
        this.membership = membership;
        menuController = new MenuController();
        orderController = new OrderController(membership);
        paymentController = new PaymentController(); //NEW


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
        updateAvailablePointsLabel();
        updateRedeemPointsCheckboxState();
        updateProceedButtonState(); //NEW
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
            menuItemsByCategory.get(category).add(new MenuItemPanel(menu, (menuItem, quantity) -> {
                updateOrderItems(menuItem, quantity);
                updateReceipt();
            }));
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
        JPanel receiptPanel = new JPanel(new BorderLayout());
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
        redeemPointsCheckbox.addActionListener(e -> updateReceipt());

        this.proceedButton = new JButton("Proceed to payment");
        proceedButton.setBackground(Color.GRAY);
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        proceedButton.addActionListener(this::showPaymentSimulation);
        rightPanel.add(redeemPointsCheckbox);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(proceedButton);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private void updateProceedButtonState() {
        proceedButton.setEnabled(!orderItems.isEmpty());
    }

    private void updateAvailablePointsLabel() {
        availablePointsLabel.setText("Available Point: " + membership.getPoints());
    }

    private void updateRedeemPointsCheckboxState() {
        redeemPointsCheckbox.setEnabled(membership.getPoints() >= 200 && !orderItems.isEmpty());
    }


    private void updateOrderItems(Menu menuItem, int quantity) {
        boolean found = false;
        for (OrderItem item : orderItems) {
            if (Objects.equals(item.getMenu().getId(), menuItem.getId())) {
                item.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem newOrderItem = new OrderItem(0, menuItem, quantity);
            orderItems.add(newOrderItem);
        }
        orderItems.removeIf(item -> item.getQuantity() <= 0);
        updateReceipt();
        updateProceedButtonState();
    }

    private void showPaymentSimulation(ActionEvent e) {
        if (orderItems.isEmpty()) {
            return; // Do nothing or show an error message
        }

        PaymentSimulationPanelView paymentPanel = new PaymentSimulationPanelView(orderItems, redeemPointsCheckbox.isSelected(), membership, this); //NEW

        JDialog dialog = new JDialog(this, "Payment Simulation", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(paymentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void updateReceipt() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        StringBuilder receiptText = new StringBuilder();
        orderTotal = 0.0;

        for (OrderItem item : orderItems) {
            String itemName = item.getMenu().getName();
            int itemQuantity = item.getQuantity();
            double itemPrice = Double.parseDouble(item.getMenu().getFormattedPrice());
            double currentItemTotal = itemPrice * itemQuantity;
            receiptText.append(itemName).append(" x ").append(itemQuantity).append("\n");
            orderTotal += currentItemTotal;
        }
        double discount = 0.0;
        double pointsRedeemed = 0;
        if(redeemPointsCheckbox.isSelected()){
            double availablePoints = membership.getPoints();
            discount = ((availablePoints / 2) / 100);
            if(discount > orderTotal){
                discount = orderTotal;
            }
            pointsRedeemed = discount * 100 * 2;
        }
        orderTotal -= discount;
        receiptText.append("------------------------\n");
        receiptText.append("Subtotal: " + formatter.format(orderTotal + discount)+ "\n");

        if (discount > 0) {
            receiptText.append("Points Redeemed: " + (int)pointsRedeemed + "\n");
            receiptText.append("Discount Applied: " + formatter.format(discount) + "\n");
        }

        receiptTextArea.setText(receiptText.toString());
        totalLabel.setText("Total: " + formatter.format(orderTotal));
        updateAvailablePointsLabel();
        updateRedeemPointsCheckboxState();
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
            quantityLabel.setPreferredSize(new Dimension(30, 20));

            JButton plusButton = new JButton("+");
            plusButton.setBackground(Color.GRAY);
            plusButton.setForeground(Color.WHITE);

            quantityPanel.add(minusButton);
            quantityPanel.add(quantityLabel);
            quantityPanel.add(plusButton);

            add(quantityPanel, gbc);

            plusButton.addActionListener(e -> {
                int currentQuantity = Integer.parseInt(quantityLabel.getText());
                int newQuantity = currentQuantity + 1;
                quantityLabel.setText(String.valueOf(newQuantity));
                quantityChangeListener.onQuantityChange(menu, newQuantity);
            });

            minusButton.addActionListener(e -> {
                int currentQuantity = Integer.parseInt(quantityLabel.getText());
                int newQuantity = currentQuantity > 0 ? currentQuantity - 1 : 0;
                quantityLabel.setText(String.valueOf(newQuantity));
                quantityChangeListener.onQuantityChange(menu, newQuantity);
            });
        }
    }

    private interface QuantityChangeListener {
        void onQuantityChange(Menu menu, int quantity);
    }

    public static void showMenu(Membership membership) {
        SwingUtilities.invokeLater(() -> new MenuView(membership));
    }
}