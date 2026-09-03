package com.sunrise.dao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AppointmentDAOTest {

    @Test
    void validateAppointmentInfo_returnsTrueForValidData() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        boolean result = appointmentDAO.validateAppointmentInfo(
                "John Silva", "0771234567", "Cleaning", "2026-09-10", "10:30");

        assertTrue(result);
    }

    @Test
    void validateAppointmentInfo_returnsFalseForEmptyPatientName() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        boolean result = appointmentDAO.validateAppointmentInfo(
                "", "0771234567", "Cleaning", "2026-09-10", "10:30");

        assertFalse(result);
    }

    @Test
    void validateAppointmentInfo_returnsFalseForEmptyContactNumber() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        boolean result = appointmentDAO.validateAppointmentInfo(
                "John Silva", "", "Cleaning", "2026-09-10", "10:30");

        assertFalse(result);
    }

    @Test
    void validateAppointmentInfo_returnsFalseForEmptyTreatmentType() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        boolean result = appointmentDAO.validateAppointmentInfo(
                "John Silva", "0771234567", "", "2026-09-10", "10:30");

        assertFalse(result);
    }
}