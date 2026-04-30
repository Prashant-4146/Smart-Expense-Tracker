package com.tracker.util;

import java.sql.Connection;
import java.sql.Statement;

public class DBUpdater {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("DROP TABLE IF EXISTS expenses");
            stmt.execute("DROP TABLE IF EXISTS budgets");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS budgets (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "monthly_limit DOUBLE NOT NULL DEFAULT 0.0," +
                    "user_id INT NOT NULL UNIQUE," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ")");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "amount DOUBLE NOT NULL," +
                    "main_category VARCHAR(100) NOT NULL," +
                    "sub_category VARCHAR(100) NOT NULL," +
                    "date DATE NOT NULL," +
                    "description TEXT," +
                    "user_id INT NOT NULL," +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                    ")");
            
            System.out.println("Database updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
