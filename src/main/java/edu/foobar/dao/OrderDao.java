package edu.foobar.dao;

import edu.foobar.models.Enums;
import edu.foobar.models.Membership;
import edu.foobar.models.Order;
import edu.foobar.models.OrderItem;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements Dao<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderDao.class);
    private Connection connection;
    private final MembershipDao membershipDao;
    private final OrderItemDao orderItemDao;

    public OrderDao() {
        this.connection = Database.getConnection();
        membershipDao = new MembershipDao();
        orderItemDao = new OrderItemDao();
    }

    public Order getLatestMemberOrder(int membershipId) {
        Order order = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT id AS order_id, membership_id, status AS order_status FROM orders WHERE membership_id = ? AND status = ? ORDER BY order_id DESC LIMIT 1");
            stmt.setInt(1, membershipId);
            stmt.setString(2, Enums.OrderStatus.PENDING.toString());
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                Enums.OrderStatus status = Enums.OrderStatus.valueOf(resultSet.getString("order_status"));
                Membership membership = membershipDao.get(membershipId);
                List<OrderItem> orderItems = orderItemDao.getOrderItemByOrderId(orderId);
                order = new Order(orderId, status, membership, orderItems);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return order;
    }

    @Override
    public Order get(int id) {
        Order order = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM orders WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                int membershipId = resultSet.getInt("membership_id");
                Enums.OrderStatus status = Enums.OrderStatus.valueOf(resultSet.getString("status"));
                Membership membership = membershipDao.get(membershipId);
                List<OrderItem> orderItems = orderItemDao.getOrderItemByOrderId(orderId);
                order = new Order(orderId, status, membership, orderItems);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return order;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM orders");
            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                int membershipId = resultSet.getInt("membership_id");
                Enums.OrderStatus status = Enums.OrderStatus.valueOf(resultSet.getString("status"));
                Membership membership = membershipDao.get(membershipId);
                List<OrderItem> orderItems = orderItemDao.getOrderItemByOrderId(orderId);
                Order order = new Order(orderId, status, membership, orderItems);
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orders;
    }

    @Override
    public Order save(Order order) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (membership_id, status) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, order.getMembership().getId());
            stmt.setString(2, order.getStatus().toString());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                return new Order(id, order.getStatus(), order.getMembership(), new ArrayList<OrderItem>());
            } else {
                logger.error("Failed to retrieve generated key for order.");
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error saving order: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Order update(Order order) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET membership_id=?, status=? WHERE id=?");
            stmt.setInt(1, order.getMembership().getId());
            stmt.setString(2, order.getStatus().toString());
            stmt.setInt(3, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
        return order;
    }

    @Override
    public void delete(Order order) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM orders WHERE id=?");
            stmt.setInt(1, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}