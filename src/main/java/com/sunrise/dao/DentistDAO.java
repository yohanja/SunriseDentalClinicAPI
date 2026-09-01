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
}