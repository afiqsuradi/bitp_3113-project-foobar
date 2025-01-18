package edu.foobar;

import edu.foobar.utils.Database;
import edu.foobar.utils.FlywayConfigException;
import edu.foobar.utils.FlywayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger( Main.class );

    public static void main(String[] args) {
        Database.getConnection();
        try {
            FlywayUtil.runMigrations();
        } catch (FlywayConfigException e) {
            logger.error("Application startup failed because database migration failed: " + e.getMessage(), e);
            System.exit(1);
            return;
        }
    }
}