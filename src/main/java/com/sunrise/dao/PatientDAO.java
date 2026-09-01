package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO {

    public boolean addPatient(String name, String address, String contactNumber) {
        String sql = "INSERT INTO Patient (patient_name, address, contact_number) VALUES (?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, contactNumber);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPatientById(int patientId) {
        String sql = "SELECT * FROM Patient WHERE patient_id = ?";
        StringBuilder result = new StringBuilder();

        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,patientId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                result.append("ID: ").append(rs.getInt("patient_id"))
                        .append(", Name: ").append(rs.getString("patient_name"))
                        .append(", Address: ").append(rs.getString("address"))
                        .append(", Contact: ").append(rs.getString("contact_number"));

            }else{
                result.append("No patient found with ID: ").append(patientId);
            }
        }catch (SQLException e){
            e.printStackTrace();
            result.append("Error retrieving patient");
        }
        return result.toString();
    }

//    Add lookup method for Dashboard - easy reference
    public String getAllPatientsJson() {
        String sql = "SELECT patient_id, patient_name FROM Patient";
        StringBuilder json = new StringBuilder("[");

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                first = false;
                json.append("{\"id\":").append(rs.getInt("patient_id"))
                        .append(",\"name\":\"").append(rs.getString("patient_name")).append("\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.append("]");
        return json.toString();
    }

    //find or create patient method
    public int findOrCreatePatient(String name, String address, String contactNumber) {
        String findSql = "SELECT patient_id FROM Patient WHERE contact_number = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement findStmt = conn.prepareStatement(findSql);
            findStmt.setString(1, contactNumber);
            ResultSet rs = findStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("patient_id");
            }

            String insertSql = "INSERT INTO Patient (patient_name, address, contact_number) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, name);
            insertStmt.setString(2, address);
            insertStmt.setString(3, contactNumber);
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

}