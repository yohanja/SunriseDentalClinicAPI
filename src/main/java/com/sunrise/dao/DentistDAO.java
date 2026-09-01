package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DentistDAO {

    public String getAllDentistsJson() {
        String sql = "SELECT dentist_id, dentist_name FROM Dentist";
        StringBuilder json = new StringBuilder("[");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;
                json.append("{\"id\":").append(rs.getInt("dentist_id"))
                        .append(",\"name\":\"").append(rs.getString("dentist_name")).append("\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.append("]");
        return json.toString();
    }

    public String getDentistNameById(int dentistId) {
        String sql = "SELECT dentist_name FROM Dentist WHERE dentist_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, dentistId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("dentist_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Unknown";
    }
}