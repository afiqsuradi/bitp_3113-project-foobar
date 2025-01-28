package edu.foobar.dao;

import edu.foobar.models.Enums;
import edu.foobar.models.Payment;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao implements Dao<Payment> {
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(PaymentDao.class);

    public PaymentDao() {
        this.connection = Database.getConnection();
    }

    @Override
    public Payment get(int id) {
        Payment payment = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM payments WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                long total = resultSet.getLong("total");
                Enums.PaymentStatus status = Enums.PaymentStatus.valueOf(resultSet.getString("status"));
                double point = resultSet.getDouble("point");
                payment = new Payment(id, orderId, total, status, point);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return payment;
    }

    @Override
    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM payments");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int orderId = resultSet.getInt("order_id");
                long total = resultSet.getLong("total");
                Enums.PaymentStatus status = Enums.PaymentStatus.valueOf(resultSet.getString("status"));
                double point = resultSet.getDouble("point");
                Payment payment = new Payment(id, orderId, total, status, point);
                payments.add(payment);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return payments;
    }

    @Override
    public void save(Payment payment) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO payments (order_id, total, status, point) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, payment.getOrderId());
            stmt.setLong(2, payment.getTotal());
            stmt.setString(3, payment.getStatus().toString());
            stmt.setDouble(4, payment.getPoint());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Payment payment) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE payments SET order_id=?, total=?, status=?, point=? WHERE id=?");
            stmt.setInt(1, payment.getOrderId());
            stmt.setLong(2, payment.getTotal());
            stmt.setString(3, payment.getStatus().toString());
            stmt.setDouble(4, payment.getPoint());
            stmt.setInt(5,payment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void delete(Payment payment) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM payments WHERE id=?");
            stmt.setInt(1, payment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

}
