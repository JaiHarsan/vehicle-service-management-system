package com.vehicleservice.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

public class DatabaseConnection {

    private static DataSource dataSource;
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    static {
        loadConfiguration();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found in classpath!");
        }
    }

    public static void setDataSource(DataSource ds) {
        dataSource = ds;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static void loadConfiguration() {
        Properties properties = new Properties();

        // 1. Try loading from local application.properties file
        File propFile = new File("application.properties");
        if (propFile.exists()) {
            try (InputStream input = new FileInputStream(propFile)) {
                properties.load(input);
            } catch (Exception ignored) {}
        }

        // 2. Resolve Environment Variables or fallback to properties (supports both spring.datasource.* and db.*)
        dbUrl = System.getenv("DB_URL");
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = properties.getProperty("spring.datasource.url",
                    properties.getProperty("db.url", "jdbc:mysql://localhost:3306/vehicle_service?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"));
        }

        dbUsername = System.getenv("DB_USERNAME");
        if (dbUsername == null || dbUsername.trim().isEmpty()) {
            dbUsername = properties.getProperty("spring.datasource.username",
                    properties.getProperty("db.username", "root"));
        }

        dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            dbPassword = properties.getProperty("spring.datasource.password",
                    properties.getProperty("db.password", ""));
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Database Connection Failed: " + e.getMessage());
            return false;
        }
    }
}

