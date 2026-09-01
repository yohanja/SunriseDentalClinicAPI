package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    public int getAppointmentCount() {
        String sql = "SELECT COUNT(*) AS total FROM Appointment";
        return getCount(sql);
    }

    public int getDentistCount() {
        String sql = "SELECT COUNT(*) AS total FROM Dentist";
        return getCount(sql);
    }

    public int getPatientCount() {
        String sql = "SELECT COUNT(*) AS total FROM Patient";
        return getCount(sql);
    }

    private int getCount(String sql) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getUpcomingAppointmentsJson() {
        String sql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, a.treatment_type, a.appointment_date, a.appointment_time " +
                "FROM Appointment a " +
                "JOIN Patient p ON a.patient_id = p.patient_id " +
                "JOIN Dentist d ON a.dentist_id = d.dentist_id " +
                "WHERE a.appointment_date >= CURDATE() " +
                "ORDER BY a.appointment_date ASC, a.appointment_time ASC " +
                "LIMIT 5";

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
                        .append("\"id\":").append(rs.getInt("appointment_id")).append(",")
                        .append("\"patient\":\"").append(rs.getString("patient_name")).append("\",")
                        .append("\"dentist\":\"").append(rs.getString("dentist_name")).append("\",")
                        .append("\"treatment\":\"").append(rs.getString("treatment_type")).append("\",")
                        .append("\"date\":\"").append(rs.getDate("appointment_date")).append("\",")
                        .append("\"time\":\"").append(rs.getTime("appointment_time")).append("\"")
                        .append("}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.append("]");
        return json.toString();
    }
}