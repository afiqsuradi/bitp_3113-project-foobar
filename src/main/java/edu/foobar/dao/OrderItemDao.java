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
        menuDao = new MenuDao();
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
                Menu menu = menuDao.get(menuId);
                orderItem = new OrderItem(id, orderId, menu);
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
                Menu menu = menuDao.get(menuId);
                OrderItem orderItem = new OrderItem(id, orderId, menu);
                orderItems.add(orderItem);
            }
        } catch (SQLException e){
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
                Menu menu = menuDao.get(menuId);
                OrderItem orderItem = new OrderItem(id, orderId, menu);
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
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO order_items (order_id, menu_id) VALUES (?, ?)");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenu().getId());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                orderItem = new OrderItem(id, orderItem.getOrderId(), orderItem.getMenu());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItem;
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE order_items SET order_id=?, menu_id=? WHERE id=?");
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getMenu().getId());
            stmt.setInt(3,orderItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return orderItem;
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
