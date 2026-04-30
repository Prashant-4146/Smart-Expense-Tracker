package com.tracker.dao;

import com.tracker.model.Budget;
import com.tracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetDAO {

    public Budget getBudgetByUserId(int userId) {
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Budget(
                    rs.getInt("id"),
                    rs.getDouble("monthly_limit"),
                    rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void upsertBudget(Budget budget) {
        String checkSql = "SELECT count(*) FROM budgets WHERE user_id = ?";
        String updateSql = "UPDATE budgets SET monthly_limit = ? WHERE user_id = ?";
        String insertSql = "INSERT INTO budgets (monthly_limit, user_id) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
             
            checkStmt.setInt(1, budget.getUserId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            
            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, budget.getMonthlyLimit());
                    updateStmt.setInt(2, budget.getUserId());
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setDouble(1, budget.getMonthlyLimit());
                    insertStmt.setInt(2, budget.getUserId());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
