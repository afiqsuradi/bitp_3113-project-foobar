package edu.foobar.dao;

import edu.foobar.models.OrderItem;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao implements Dao<OrderItem> {
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(OrderItemDao.class);

    public OrderItemDao() {
        this.connection = Database.getConnection();
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM order_items WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int menuId = resultSet.getInt("menu_id");
                orderItem = new OrderItem(id, orderId, menuId);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItem;
    }

    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM order_items");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int orderId = resultSet.getInt("order_id");
                int menuId = resultSet.getInt("menu_id");
                OrderItem orderItem = new OrderItem(id, orderId, menuId);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItems;
    }

    @Override
    public void save(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO order_items (order_id, menu_id) VALUES (?, ?)");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenuId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE order_items SET order_id=?, menu_id=? WHERE id=?");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenuId());
            stmt.setInt(3,orderItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void delete(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM order_items WHERE id=?");
            stmt.setInt(1, orderItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
