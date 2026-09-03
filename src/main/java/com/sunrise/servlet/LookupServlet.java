package com.sunrise.servlet;

import com.sunrise.dao.DentistDAO;
import com.sunrise.dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/lookup")
public class LookupServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("type");

        if ("patients".equals(type)) {
            PatientDAO patientDAO = new PatientDAO();
            out.print(patientDAO.getAllPatientsJson());
        } else if ("dentists".equals(type)) {
            DentistDAO dentistDAO = new DentistDAO();
            out.print(dentistDAO.getAllDentistsJson());
        } else if ("patientByContact".equals(type)) {
            String contact = request.getParameter("contact");

            if (contact == null || contact.isEmpty()) {
                out.print("{\"error\": \"Contact number is required\"}");
                return;
            }

            PatientDAO patientDAO = new PatientDAO();
            out.print(patientDAO.getPatientByContactJson(contact));
        } else {
            out.print("{\"error\": \"Invalid lookup type\"}");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (role == null || !role.equals("Admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.print("{\"error\": \"Access denied. Admin privileges required.\"}");
            return;
        }

        String action = request.getParameter("action");

        if ("addDentist".equals(action)) {
            String name = request.getParameter("name");
            String specialization = request.getParameter("specialization");

            if (name == null || name.isEmpty()) {
                out.print("{\"error\": \"Dentist name is required\"}");
                return;
            }

            DentistDAO dentistDAO = new DentistDAO();
            boolean success = dentistDAO.addDentist(name, specialization);

            if (success) {
                out.print("{\"message\": \"Dentist registered successfully\"}");
            } else {
                out.print("{\"error\": \"Failed to register dentist\"}");
            }
        } else {
            out.print("{\"error\": \"Invalid action\"}");
        }
    }
}

