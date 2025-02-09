package edu.foobar.utils;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class FlywayUtil {

    private static final Logger logger = LoggerFactory.getLogger(FlywayUtil.class);

    public static void runMigrations() {
        Properties props = Config.getConfig();

        try {
            FluentConfiguration fluentConfiguration = new FluentConfiguration();
            Flyway flyway = new Flyway(fluentConfiguration
                    .dataSource(
                            props.getProperty("db.url"),
                            props.getProperty("db.user"),
                            props.getProperty("db.password"))
                    .locations("classpath:db/migrations")
                    .defaultSchema(props.getProperty("db.name")));

            try {
                flyway.migrate();
                logger.info("Database migrations completed successfully.");
            } catch (FlywayException e) {
                logger.error("Flyway migration failed: " + e.getMessage(), e);
                try {
                    logger.warn("Attempting Flyway repair...");
                    flyway.repair();
                    logger.info("Flyway repair completed.");
                } catch (FlywayException repairException){
                    logger.error("Flyway repair failed: " + repairException.getMessage(), repairException);
                    throw new FlywayConfigException("Flyway migration and repair failed!", e);

                }
            }
        } catch (Exception e) {
            logger.error("Error configuring Flyway: " + e.getMessage(), e);
            throw new FlywayConfigException("Flyway configuration failed!", e);
        }
    }
}