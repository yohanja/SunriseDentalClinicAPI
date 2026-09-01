package com.sunrise.servlet;

import com.sunrise.dao.AppointmentDAO;
import com.sunrise.dao.DentistDAO;
import com.sunrise.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String patientName = request.getParameter("patientName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String email = request.getParameter("email");
        String dentistIdParam = request.getParameter("dentistId");
        String treatmentType = request.getParameter("treatmentType");
        String appointmentDate = request.getParameter("appointmentDate");
        String appointmentTime = request.getParameter("appointmentTime");

        if (patientName == null || address == null || contactNumber == null || dentistIdParam == null
                || treatmentType == null || appointmentDate == null || appointmentTime == null) {
            out.print("{\"error\": \"Missing required fields\"}");
            return;
        }

        int dentistId = Integer.parseInt(dentistIdParam);

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        int appointmentId = appointmentDAO.addAppointment(patientName, address, contactNumber, email,
                dentistId, treatmentType, appointmentDate, appointmentTime);

        if (appointmentId != -1) {
            DentistDAO dentistDAO = new DentistDAO();
            String dentistName = dentistDAO.getDentistNameById(dentistId);

            EmailService.sendAppointmentConfirmation(email, appointmentId, patientName,
                    dentistName, treatmentType, appointmentDate, appointmentTime);

            out.print("{\"message\": \"Appointment booked successfully. Appointment ID: " + appointmentId + "\"}");
        } else {
            out.print("{\"error\": \"Failed to book appointment\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");
        String contactParam = request.getParameter("contact");

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        if (idParam != null) {
            int appointmentId = Integer.parseInt(idParam);
            String result = appointmentDAO.getAppointmentById(appointmentId);
            out.print("{\"result\": \"" + result + "\"}");

        } else if (contactParam != null) {
            String result = appointmentDAO.getAppointmentsByContact(contactParam);
            out.print("{\"results\": " + result + "}");

        } else {
            out.print("{\"error\": \"Please provide either an appointment id or a contact number\"}");
        }
    }
}