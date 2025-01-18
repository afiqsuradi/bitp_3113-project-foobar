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
        Properties props = DotEnv.getConfig();

        try {
            FluentConfiguration fluentConfiguration = new FluentConfiguration();
            Flyway flyway = new Flyway(
                    fluentConfiguration.dataSource(
                                    props.getProperty("db.url"),
                                    props.getProperty("db.user"),
                                    props.getProperty("db.password"))
                            .locations("classpath:db/migrations")
                            .defaultSchema("fos")
            );
            flyway.migrate();
            logger.info("Database migrations completed successfully.");
        } catch (FlywayException e) {
            logger.error("Flyway migration failed: " + e.getMessage(), e);
            throw new FlywayConfigException("Flyway migration failed!", e);
        }
    }
}