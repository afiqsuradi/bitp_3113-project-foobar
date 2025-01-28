package edu.foobar.dao;

import edu.foobar.models.Enums;
import edu.foobar.models.Order;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao implements Dao<Order>{
    private static final Logger logger = LoggerFactory.getLogger( OrderDao.class );
    private Connection connection;

    public OrderDao() {
        this.connection = Database.getConnection();
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
                order = new Order(orderId, membershipId, status);
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
                Order order = new Order(orderId, membershipId, status);
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orders;
    }

    @Override
    public void save(Order order) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO orders (membership_id, status) VALUES (?, ?)");
            stmt.setInt(1, order.getMembershipId());
            stmt.setString(2, order.getStatus().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Order order) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE orders SET membership_id=?, status=? WHERE id=?");
            stmt.setInt(1, order.getMembershipId());
            stmt.setString(2, order.getStatus().toString());
            stmt.setInt(3, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
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
