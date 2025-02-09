package edu.foobar.controllers;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import edu.foobar.dao.OrderItemDao;
import edu.foobar.dao.PaymentDao;
import edu.foobar.dao.MembershipDao;
import edu.foobar.controllers.OrderController;
import edu.foobar.models.Enums;
import edu.foobar.models.Membership;
import edu.foobar.models.Order;
import edu.foobar.models.OrderItem;
import edu.foobar.models.Payment;
import edu.foobar.views.MenuView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JFrame;


public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final OrderItemDao orderItemDao = new OrderItemDao();
    private final PaymentDao paymentDao = new PaymentDao();
    private final MembershipDao membershipDao = new MembershipDao();

    public void processPayment(List<OrderItem> orderItems, double orderTotalDecimal, boolean redeemPoints, Membership membership, MenuView menuView) throws Exception {

        long orderTotal = (long) (orderTotalDecimal * 100);
        double pointsRedeemed = 0.0;
        double rewardPoints = 0.0;

        try {

            OrderController orderController = new OrderController(membership);
            Order currentOrder = OrderController.getCurrentOrder();

            for (OrderItem item : orderItems) {
                item.setOrderId(currentOrder.getId());
                orderItemDao.save(item);
            }

            double discount = 0.0;
            double availablePoints = membership.getPoints();

            if (redeemPoints) {
                discount = (availablePoints / 2) / 100;
                if (discount > orderTotalDecimal) {
                    discount = orderTotalDecimal;
                }
                pointsRedeemed = discount * 100 * 2;

            }

            long totalAfterDiscount = orderTotal - (long)(discount * 100);
            double totalAfterDiscountDecimal = (double) totalAfterDiscount / 100;

            if(!redeemPoints) {
                rewardPoints = (double) totalAfterDiscount / 100;
                rewardPoints = rewardPoints * 2;
            }

            Payment payment = new Payment(currentOrder.getId(), totalAfterDiscount, Enums.PaymentStatus.PAID, pointsRedeemed);
            paymentDao.save(payment);


            double newPoints = membership.getPoints();

            if(redeemPoints){
                newPoints = newPoints - pointsRedeemed;
            }
            if(!redeemPoints){
                newPoints = newPoints + rewardPoints;
            }

            membership.setPoints(newPoints);
            membershipDao.update(membership);

            currentOrder.setStatus(Enums.OrderStatus.DONE);
            orderController.updateOrder(currentOrder);

            generatePdfReceipt(orderItems, orderTotalDecimal, discount, membership, rewardPoints,menuView);
        } catch (Exception e) {
            logger.error("Error processing payment: " + e.getMessage(), e);
            throw new Exception("Payment processing failed", e);
        }
    }


    private void generatePdfReceipt(List<OrderItem> orderItems, double orderTotal, double discount, Membership membership, double rewardPoints, JFrame frame) throws IOException {
        String homeDir = System.getProperty("user.dir");
        String receiptsDir = homeDir + File.separator + "receipts";
        File directory = new File(receiptsDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = receiptsDir + File.separator + "receipt_" + System.currentTimeMillis() + ".pdf";
        try (PdfWriter writer = new PdfWriter(filename)) {
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();

            document.add(new Paragraph("Receipt"));
            document.add(new Paragraph("Membership ID: " + membership.getId()));
            document.add(new Paragraph("Email: " + membership.getCustomerEmail()));
            document.add(new Paragraph("-----------------------------------"));

            for (OrderItem item : orderItems) {
                document.add(new Paragraph(item.getMenu().getName() + " x " + item.getQuantity() + "  " + formatter.format((double)item.getMenu().getPrice() / 100 * item.getQuantity())));
            }

            document.add(new Paragraph("-----------------------------------"));
            document.add(new Paragraph("Subtotal: " + formatter.format(orderTotal)));
            if (discount > 0) {
                document.add(new Paragraph("Points Redeemed: " + (int) (discount * 100 * 2)));
                document.add(new Paragraph("Discount Applied: " + formatter.format(discount)));
            }

            if(rewardPoints > 0) {
                document.add(new Paragraph("Reward Points Earned: " + rewardPoints));
            }
            document.add(new Paragraph("Total: " + formatter.format(orderTotal - discount)));
            document.close();

        } catch (IOException e) {
            logger.error("Error generating PDF receipt: " + e.getMessage(), e);
            throw e;
        }
    }
}