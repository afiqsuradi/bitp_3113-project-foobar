package edu.foobar.views;

import edu.foobar.controllers.PaymentController;
import edu.foobar.models.Membership;
import edu.foobar.models.OrderItem;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.foobar.controllers.OrderController;

public class PaymentSimulationPanelView extends JPanel {

    private final List<OrderItem> orderItems;
    private double orderTotal;
    private final boolean redeemPoints;
    private final Membership membership;
    private final JFrame parentFrame;
    private final Logger logger = LoggerFactory.getLogger(PaymentSimulationPanelView.class);


    public PaymentSimulationPanelView(List<OrderItem> orderItems, boolean redeemPoints, Membership membership, JFrame parentFrame) {
        this.orderItems = orderItems;
        this.redeemPoints = redeemPoints;
        this.membership = membership;
        this.parentFrame = parentFrame;

        for (OrderItem item : orderItems) {
            String itemName = item.getMenu().getName();
            int itemQuantity = item.getQuantity();
            double itemPrice = Double.parseDouble(item.getMenu().getFormattedPrice());
            double currentItemTotal = itemPrice * itemQuantity;
            this.orderTotal += currentItemTotal;
        }

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea orderSummary = new JTextArea();
        orderSummary.setEditable(false);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        StringBuilder sb = new StringBuilder();

        for (OrderItem item : orderItems) {
            sb.append(item.getMenu().getName()).append(" x ").append(item.getQuantity()).append("\n");
        }
        sb.append("----------------------\n");

        double discount = 0.0;
        double pointsRedeemed = 0.0;
        if (redeemPoints) {
            double availablePoints = membership.getPoints();
            discount = ((availablePoints / 2) / 100);
            if (discount > orderTotal) {
                discount = orderTotal;
            }
            pointsRedeemed = discount * 100 * 2;
            sb.append("Points Redeemed: ").append((int)pointsRedeemed).append("\n");
            sb.append("Discount Applied: ").append(formatter.format(discount)).append("\n");
        }

        sb.append("Subtotal: ").append(formatter.format(orderTotal)).append("\n");
        sb.append("Total: ").append(formatter.format(orderTotal - discount)).append("\n");

        orderSummary.setText(sb.toString());
        add(new JScrollPane(orderSummary), BorderLayout.CENTER);

        JButton payButton = new JButton("Pay");
        payButton.addActionListener(e -> processPayment());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(payButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processPayment() {

        PaymentController paymentController = new PaymentController();
        OrderController orderController = new OrderController(membership);

        try {
            paymentController.processPayment(orderItems, orderTotal, redeemPoints, membership, (MenuView) parentFrame);

            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }

            JOptionPane.showMessageDialog(parentFrame, "Payment successful! Receipt generated.", "Success", JOptionPane.INFORMATION_MESSAGE);

            parentFrame.dispose();
            LoginView.showLogin();
        } catch (Exception ex) {
            logger.error("Payment processing failed: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(parentFrame, "Payment processing failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}