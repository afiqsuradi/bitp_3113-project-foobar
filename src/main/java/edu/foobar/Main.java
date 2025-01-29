package edu.foobar;

import edu.foobar.utils.Database;
import edu.foobar.utils.FlywayConfigException;
import edu.foobar.utils.FlywayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger( Main.class );

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = Database.getConnection();
            if (connection != null) {
                logger.info("Database connection established successfully!");
                // Write business logic HERE V
            } else {
                logger.error("Failed to establish database connection.");
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred: " + e.getMessage(), e);
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection " + e.getMessage());
                }
            }
        }
    }
}