package com.sunrise;

import com.sunrise.dao.AppointmentDAO;

public class Main {
    public static void main(String[] args) {
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        boolean success = appointmentDAO.addAppointment(1, 1, "Cleaning", "2026-09-01", "10:30:00");

        if (success) {
            System.out.println("Appointment added successfully!");
        } else {
            System.out.println("Failed to add appointment.");
        }
    }
}