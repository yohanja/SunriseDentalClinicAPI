package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}