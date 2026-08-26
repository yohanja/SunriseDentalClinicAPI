package com.sunrise;

import com.sunrise.dao.PatientDAO;
import com.sunrise.util.DBConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        PatientDAO patientDAO = new PatientDAO();

        String patientInfo = patientDAO.getPatientById(1);
        System.out.println(patientInfo);
    }
}