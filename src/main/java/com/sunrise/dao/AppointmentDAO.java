package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppointmentDAO {

    public boolean addAppointment(int patientId, int dentistId, String treatmentType,
                                  String appointmentDate, String appointmentTime) {

        String sql = "INSERT INTO Appointment (patient_id, dentist_id, treatment_type, appointment_date, appointment_time) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, treatmentType);
            stmt.setString(4, appointmentDate);
            stmt.setString(5, appointmentTime);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}