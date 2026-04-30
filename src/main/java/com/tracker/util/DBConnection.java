package com.tracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "Mysql@4146"; // Update as necessary

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Ensure driver is loaded
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
