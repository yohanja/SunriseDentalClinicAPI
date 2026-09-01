package com.sunrise.servlet;

import com.sunrise.dao.AppointmentDAO;
import com.sunrise.dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/patient")
public class PatientServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        String searchParam = request.getParameter("search");

        PatientDAO patientDAO = new PatientDAO();

        if (idParam != null) {
            int patientId = Integer.parseInt(idParam);

            String patientJson = patientDAO.getPatientDetailsJson(patientId);

            AppointmentDAO appointmentDAO = new AppointmentDAO();
            String historyJson = appointmentDAO.getAppointmentHistoryByPatientId(patientId);

            String combined = patientJson.substring(0, patientJson.length() - 1)
                    + ",\"history\":" + historyJson + "}";

            out.print(combined);

        } else if (searchParam != null) {
            String resultsJson = patientDAO.searchPatientsJson(searchParam);
            out.print("{\"results\": " + resultsJson + "}");

        } else {
            out.print("{\"error\": \"Please provide either an id or a search query\"}");
        }
    }
}