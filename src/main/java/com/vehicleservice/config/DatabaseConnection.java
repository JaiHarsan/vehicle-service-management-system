package com.vehicleservice.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

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

    private static void loadConfiguration() {
        Properties properties = new Properties();

        // 1. Try loading from local application.properties file
        File propFile = new File("application.properties");
        if (propFile.exists()) {
            try (InputStream input = new FileInputStream(propFile)) {
                properties.load(input);
            } catch (Exception ignored) {}
        }

        // 2. Resolve Environment Variables or fallback to properties
        dbUrl = System.getenv("DB_URL");
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = properties.getProperty("db.url", "jdbc:mysql://localhost:3306/vehicle_service?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        }

        dbUsername = System.getenv("DB_USERNAME");
        if (dbUsername == null || dbUsername.trim().isEmpty()) {
            dbUsername = properties.getProperty("db.username", "root");
        }

        dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            dbPassword = properties.getProperty("db.password", "");
        }
    }

    public static Connection getConnection() throws SQLException {
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
