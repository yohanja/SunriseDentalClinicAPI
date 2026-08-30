package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDAO {

    private double getPriceForTreatment(String treatmentType) {
        switch (treatmentType) {
            case "Cleaning":
                return 3000.00;
            case "Filling":
                return 5000.00;
            case "Extraction":
                return 4000.00;
            default:
                return 2000.00;
        }
    }

    public String generateBill(int appointmentId) {
        String getTreatmentSql = "SELECT treatment_type FROM Appointment WHERE appointment_id = ?";
        String insertBillSql = "INSERT INTO Bill (appointment_id, amount, payment_status) VALUES (?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement getStmt = conn.prepareStatement(getTreatmentSql);
            getStmt.setInt(1, appointmentId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                return "No appointment found with ID: " + appointmentId;
            }

            String treatmentType = rs.getString("treatment_type");
            double amount = getPriceForTreatment(treatmentType);

            PreparedStatement insertStmt = conn.prepareStatement(insertBillSql);
            insertStmt.setInt(1, appointmentId);
            insertStmt.setDouble(2, amount);
            insertStmt.setString(3, "Unpaid");

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                return "Bill generated. Treatment: " + treatmentType + ", Amount: " + amount;
            } else {
                return "Failed to generate bill.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error generating bill.";
        }
    }
}