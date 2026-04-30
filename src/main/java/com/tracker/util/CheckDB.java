package com.tracker.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class CheckDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "expenses", null);
            while (rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
