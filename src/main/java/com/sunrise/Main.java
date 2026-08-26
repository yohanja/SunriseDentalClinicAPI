package com.sunrise;

import com.sunrise.dao.PatientDAO;
import com.sunrise.util.DBConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        PatientDAO patientDAO = new PatientDAO();

        boolean success = patientDAO.addPatient("Nimal Perera", "123 Galle Road, Colombo", "0771234567");

        if (success) {
            System.out.println("Patient added successfully!");
        } else {
            System.out.println("Failed to add patient.");
        }
    }
}
