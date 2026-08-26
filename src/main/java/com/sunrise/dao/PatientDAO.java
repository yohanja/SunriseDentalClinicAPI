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

}