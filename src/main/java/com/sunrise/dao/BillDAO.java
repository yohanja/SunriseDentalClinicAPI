package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDAO {

    private static final double CONSULTATION_FEE = 1000.00;

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
            double treatmentCost = getPriceForTreatment(treatmentType);
            double totalAmount = treatmentCost + CONSULTATION_FEE;

            PreparedStatement insertStmt = conn.prepareStatement(insertBillSql);
            insertStmt.setInt(1, appointmentId);
            insertStmt.setDouble(2, totalAmount);
            insertStmt.setString(3, "Unpaid");

            int rowsInserted = insertStmt.executeUpdate();

            if (rowsInserted > 0) {
                return "Bill generated. Treatment: " + treatmentType + ", Amount: " + totalAmount;
            } else {
                return "Failed to generate bill.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error generating bill.";
        }
    }

    public String getBillDetailsJson(int billId) {
        String sql = "SELECT b.bill_id, b.amount, b.payment_status, " +
                "a.appointment_id, a.treatment_type, a.appointment_date, a.appointment_time, " +
                "p.patient_name, p.address, p.contact_number, " +
                "d.dentist_name " +
                "FROM Bill b " +
                "JOIN Appointment a ON b.appointment_id = a.appointment_id " +
                "JOIN Patient p ON a.patient_id = p.patient_id " +
                "JOIN Dentist d ON a.dentist_id = d.dentist_id " +
                "WHERE b.bill_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, billId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String treatmentType = rs.getString("treatment_type");
                double treatmentCost = getPriceForTreatment(treatmentType);

                return "{"
                        + "\"billId\":" + rs.getInt("bill_id") + ","
                        + "\"appointmentId\":" + rs.getInt("appointment_id") + ","
                        + "\"patientName\":\"" + rs.getString("patient_name") + "\","
                        + "\"address\":\"" + rs.getString("address") + "\","
                        + "\"contactNumber\":\"" + rs.getString("contact_number") + "\","
                        + "\"dentistName\":\"" + rs.getString("dentist_name") + "\","
                        + "\"treatmentType\":\"" + treatmentType + "\","
                        + "\"appointmentDate\":\"" + rs.getDate("appointment_date") + "\","
                        + "\"appointmentTime\":\"" + rs.getTime("appointment_time") + "\","
                        + "\"treatmentCost\":" + treatmentCost + ","
                        + "\"consultationFee\":" + CONSULTATION_FEE + ","
                        + "\"amount\":" + rs.getDouble("amount") + ","
                        + "\"paymentStatus\":\"" + rs.getString("payment_status") + "\""
                        + "}";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "{\"error\": \"Bill not found\"}";
    }

    public boolean markAsPaid(int billId) {
        String sql = "UPDATE Bill SET payment_status = 'Paid' WHERE bill_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, billId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}