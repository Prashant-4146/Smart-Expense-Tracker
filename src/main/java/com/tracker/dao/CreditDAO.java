package com.tracker.dao;

import com.tracker.model.Credit;
import com.tracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditDAO {

    public Credit getCreditByUserId(int userId) {
        String sql = "SELECT * FROM credits WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Credit(
                    rs.getInt("id"),
                    rs.getDouble("total_amount"),
                    rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCredit(Credit credit) {
        String sql = "UPDATE credits SET total_amount = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setDouble(1, credit.getTotalAmount());
            pstmt.setInt(2, credit.getUserId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCredit(Credit credit) {
        String sql = "INSERT INTO credits (total_amount, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setDouble(1, credit.getTotalAmount());
            pstmt.setInt(2, credit.getUserId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
