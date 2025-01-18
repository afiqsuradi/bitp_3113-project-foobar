package edu.foobar.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database{

    private static Connection connection;
    public static Connection getConnection() {
        if(connection == null){
            try{
                Properties props = DotEnv.getConfig();
                String dbURL = props.getProperty("db.url");
                String dbUser = props.getProperty("db.user");
                String dbPass = props.getProperty("db.password");
                connection = DriverManager.getConnection(dbURL, dbUser, dbPass);
            }catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}