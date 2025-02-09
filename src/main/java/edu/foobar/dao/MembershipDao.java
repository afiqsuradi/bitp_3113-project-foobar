package edu.foobar.dao;

import edu.foobar.models.Membership;
import edu.foobar.utils.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembershipDao implements Dao<Membership> {
    private static final Logger logger = LoggerFactory.getLogger( MembershipDao.class );
    private Connection connection;

    public MembershipDao() {
        this.connection = Database.getConnection();
    }

    @Override
    public Membership get(int id) {
        Membership membership = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM memberships WHERE id=?");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int membershipId = resultSet.getInt("id");
                String customerEmail = resultSet.getString("customer_email");
                double points = resultSet.getDouble("points");
                membership = new Membership(membershipId, customerEmail, points);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return membership;
    }

    public Membership get(String email) {
        Membership membership = null;
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM memberships WHERE customer_email=?");
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int membershipId = resultSet.getInt("id");
                String customerEmail = resultSet.getString("customer_email");
                double points = resultSet.getDouble("points");
                membership = new Membership(membershipId, customerEmail, points);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return membership;
    }

    @Override
    public List<Membership> getAll() {
        List<Membership> memberships = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM memberships");
            while (resultSet.next()) {
                int membershipId = resultSet.getInt("id");
                String customerEmail = resultSet.getString("customer_email");
                double points = resultSet.getDouble("points");
                Membership membership = new Membership(membershipId, customerEmail, points);
                memberships.add(membership);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return memberships;
    }

    @Override
    public Membership save(Membership membership) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO memberships (customer_email, points) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, membership.getCustomerEmail());
            stmt.setDouble(2, membership.getPoints());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                membership = new Membership(id, membership.getCustomerEmail(), membership.getPoints());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return membership;
    }

    @Override
    public Membership update(Membership membership) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE memberships SET customer_email=?, points=? WHERE id=?");
            stmt.setString(1, membership.getCustomerEmail());
            stmt.setDouble(2, membership.getPoints());
            stmt.setInt(3, membership.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return membership;
    }

    @Override
    public void delete(Membership membership) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM memberships WHERE id=?");
            stmt.setInt(1, membership.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}