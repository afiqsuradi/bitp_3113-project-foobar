package edu.foobar.dao;

import edu.foobar.models.Menu;
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
    private MenuDao menuDao;

    public OrderItemDao() {
        this.connection = Database.getConnection();
        this.menuDao = new MenuDao();
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT FROM order_items WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int menuId = resultSet.getInt("menu_id");
                int quantity = resultSet.getInt("quantity");
                Menu menu = menuDao.get(menuId);
                orderItem = new OrderItem(id, orderId, menu, quantity);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItem;
    }

    public List<OrderItem> getOrderItemByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM order_items WHERE order_id=?");
            stmt.setInt(1, orderId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int menuId = resultSet.getInt("menu_id");
                int quantity = resultSet.getInt("quantity"); // Retrieve quantity
                Menu menu = menuDao.get(menuId);
                OrderItem orderItem = new OrderItem(id, orderId, menu, quantity);  // Use the constructor
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItems;
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
                int quantity = resultSet.getInt("quantity");  // Retrieve quantity
                Menu menu = menuDao.get(menuId);
                OrderItem orderItem = new OrderItem(id, orderId, menu, quantity); // Use the constructor
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItems;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO order_items (order_id, menu_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenu().getId());
            stmt.setInt(3, orderItem.getQuantity());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new OrderItem(id, orderItem.getOrderId(), orderItem.getMenu(), orderItem.getQuantity());
                } else {
                    throw new SQLException("Creating order item failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE order_items SET order_id=?, menu_id=?, quantity=? WHERE id=?");

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenu().getId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setInt(4, orderItem.getId());

            stmt.executeUpdate();

            return orderItem;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
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