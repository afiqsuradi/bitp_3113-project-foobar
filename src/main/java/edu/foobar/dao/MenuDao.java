package edu.foobar.dao;

import edu.foobar.models.Enums;
import edu.foobar.models.Menu;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDao implements Dao<Menu> {
    private final Connection connection;
    private static final Logger logger = LoggerFactory.getLogger(MenuDao.class);

    public MenuDao() {
        this.connection = Database.getConnection();
    }

    @Override
    public Menu get(int id) {
        Menu menu = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM menus WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int menuId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                long price = resultSet.getLong("price");
                Enums.FoodCategory foodCategory = Enums.FoodCategory.valueOf(resultSet.getString("category"));
                menu = new Menu(menuId, name, price, foodCategory);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return menu;
    }

    @Override
    public List<Menu> getAll() {
        List<Menu> menus = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM menus");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                long price = resultSet.getLong("price");
                Enums.FoodCategory foodCategory = Enums.FoodCategory.valueOf(resultSet.getString("category"));
                Menu menu = new Menu(id, name, price, foodCategory);
                menus.add(menu);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return menus;
    }

    @Override
    public Menu save(Menu menu) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO menus (name, price, category) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, menu.getName());
            stmt.setLong(2, menu.getPrice());
            stmt.setString(3, menu.getCategory().toString());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                menu = new Menu(id, menu.getName(), menu.getPrice(), menu.getCategory());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
        return menu;
    }

    @Override
    public Menu update(Menu menu) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE menus SET name=?, price=?, category=? WHERE id=?");
            stmt.setString(1, menu.getName());
            stmt.setLong(2, menu.getPrice());
            stmt.setString(3, menu.getCategory().toString());
            stmt.setInt(4, menu.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return null;
        }
        return menu;
    }


    @Override
    public void delete(Menu menu) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM menus WHERE id=?");
            stmt.setInt(1, menu.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}