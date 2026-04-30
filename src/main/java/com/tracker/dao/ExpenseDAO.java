package com.tracker.dao;

import com.tracker.model.Expense;
import com.tracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (amount, main_category, sub_category, date, description, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getMainCategory());
            pstmt.setString(3, expense.getSubCategory());
            pstmt.setDate(4, expense.getDate());
            pstmt.setString(5, expense.getDescription());
            pstmt.setInt(6, expense.getUserId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getExpensesByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                expenses.add(new Expense(
                    rs.getInt("id"),
                    rs.getDouble("amount"),
                    rs.getString("main_category"),
                    rs.getString("sub_category"),
                    rs.getDate("date"),
                    rs.getString("description"),
                    rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET amount = ?, main_category = ?, sub_category = ?, date = ?, description = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getMainCategory());
            pstmt.setString(3, expense.getSubCategory());
            pstmt.setDate(4, expense.getDate());
            pstmt.setString(5, expense.getDescription());
            pstmt.setInt(6, expense.getId());
            pstmt.setInt(7, expense.getUserId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int expenseId, int userId) {
        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, expenseId);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
