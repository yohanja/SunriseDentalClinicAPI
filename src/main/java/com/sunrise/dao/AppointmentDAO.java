package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {

    public int addAppointment(String patientName, String address, String contactNumber, String email,
                              int dentistId, String treatmentType,
                              String appointmentDate, String appointmentTime) {

        PatientDAO patientDAO = new PatientDAO();
        int patientId = patientDAO.findOrCreatePatient(patientName, address, contactNumber, email);

        if (patientId == -1) {
            return -1;
        }

        String sql = "INSERT INTO Appointment (patient_id, dentist_id, treatment_type, appointment_date, appointment_time) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, treatmentType);
            stmt.setString(4, appointmentDate);
            stmt.setString(5, appointmentTime);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getAppointmentById(int appointmentId) {
        String sql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, a.treatment_type, a.appointment_date, a.appointment_time " +
                "FROM Appointment a " +
                "JOIN Patient p ON a.patient_id = p.patient_id " +
                "JOIN Dentist d ON a.dentist_id = d.dentist_id " +
                "WHERE a.appointment_id = ?";

        StringBuilder result = new StringBuilder();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointmentId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result.append("Appointment ID: ").append(rs.getInt("appointment_id"))
                        .append(", Patient: ").append(rs.getString("patient_name"))
                        .append(", Dentist: ").append(rs.getString("dentist_name"))
                        .append(", Treatment: ").append(rs.getString("treatment_type"))
                        .append(", Date: ").append(rs.getDate("appointment_date"))
                        .append(", Time: ").append(rs.getTime("appointment_time"));
            } else {
                result.append("No appointment found with ID: ").append(appointmentId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result.append("Error retrieving appointment.");
        }

        return result.toString();
    }
    public String getAppointmentsByContact(String contactNumber) {
        String sql = "SELECT a.appointment_id, p.patient_name, d.dentist_name, a.treatment_type, a.appointment_date, a.appointment_time " +
                "FROM Appointment a " +
                "JOIN Patient p ON a.patient_id = p.patient_id " +
                "JOIN Dentist d ON a.dentist_id = d.dentist_id " +
                "WHERE p.contact_number = ? " +
                "ORDER BY a.appointment_date DESC";

        StringBuilder json = new StringBuilder("[");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, contactNumber);

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

    public String getAppointmentHistoryByPatientId(int patientId) {
        String sql = "SELECT a.appointment_id, d.dentist_name, a.treatment_type, a.appointment_date, a.appointment_time " +
                "FROM Appointment a " +
                "JOIN Dentist d ON a.dentist_id = d.dentist_id " +
                "WHERE a.patient_id = ? " +
                "ORDER BY a.appointment_date DESC";

        StringBuilder json = new StringBuilder("[");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{")
                        .append("\"id\":").append(rs.getInt("appointment_id")).append(",")
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