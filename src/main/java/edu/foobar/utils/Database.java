package edu.foobar.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database{
    private static final Logger logger = LoggerFactory.getLogger( Database.class );
    private static Connection connection;
    public static Connection getConnection() {
        if(connection == null){
            try{
                Properties props = Config.getConfig();
                String dbURL = props.getProperty("db.url");
                String dbName = props.getProperty("db.name");
                String dbUser = props.getProperty("db.user");
                String dbPass = props.getProperty("db.password");
                String fullDbURL = dbURL + "/" + dbName;
                connection = DriverManager.getConnection( fullDbURL, dbUser, dbPass);
            }catch(SQLException e) {
               logger.error(e.getMessage());
            }
        }
        return connection;
    }
}