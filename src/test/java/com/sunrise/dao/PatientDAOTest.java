package com.sunrise.dao;

import com.sunrise.util.DBConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PatientDAOTest {

    private static final String TEST_CONTACT = "0000000001";

    @AfterEach
    void cleanUpTestData() throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Patient WHERE contact_number = ?");
        stmt.setString(1, TEST_CONTACT);
        stmt.executeUpdate();
    }

    @Test
    void findOrCreatePatient_createsNewPatientWhenNotFound() {
        PatientDAO patientDAO = new PatientDAO();

        int patientId = patientDAO.findOrCreatePatient(
                "Test Patient", "123 Test Street", TEST_CONTACT, "test@example.com");

        assertNotEquals(-1, patientId);
    }

    @Test
    void findOrCreatePatient_returnsSameIdForSameContactNumber() {
        PatientDAO patientDAO = new PatientDAO();

        int firstCallId = patientDAO.findOrCreatePatient(
                "Test Patient", "123 Test Street", TEST_CONTACT, "test@example.com");

        int secondCallId = patientDAO.findOrCreatePatient(
                "Different Name", "Different Address", TEST_CONTACT, "different@example.com");

        assertEquals(firstCallId, secondCallId);
    }
}