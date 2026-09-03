package com.sunrise.dao;

import com.sun.source.tree.CatchTree;
import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public String validateLogin(String username, String password){
        String sql = "SELECT role FROM User WHERE username = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getString("role");
            }else{
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getUserProfileJson(String username) {
        String sql = "SELECT user_id, username, role FROM User WHERE username = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return "{"
                        + "\"userId\":" + rs.getInt("user_id") + ","
                        + "\"username\":\"" + rs.getString("username") + "\","
                        + "\"role\":\"" + rs.getString("role") + "\""
                        + "}";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "{\"error\": \"User not found\"}";
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        String checkSql = "SELECT user_id FROM User WHERE username = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            checkStmt.setString(2, currentPassword);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String updateSql = "UPDATE User SET password = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, username);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getAllUsersJson() {
        String sql = "SELECT user_id, username, role FROM User ORDER BY user_id";
        StringBuilder json = new StringBuilder("[");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;
                json.append("{")
                        .append("\"id\":").append(rs.getInt("user_id")).append(",")
                        .append("\"username\":\"").append(rs.getString("username")).append("\",")
                        .append("\"role\":\"").append(rs.getString("role")).append("\"")
                        .append("}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.append("]");
        return json.toString();
    }

    public boolean registerUser(String username, String password, String role) {
        String checkSql = "SELECT user_id FROM User WHERE username = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            String insertSql = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, role);

            int rowsInserted = insertStmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean adminResetPassword(String username, String newPassword) {
        String sql = "UPDATE User SET password = ? WHERE username = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
