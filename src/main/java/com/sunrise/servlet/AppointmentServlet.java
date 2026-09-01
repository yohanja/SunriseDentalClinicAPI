package com.sunrise.servlet;

import com.sunrise.dao.AppointmentDAO;
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
        boolean success = appointmentDAO.addAppointment(patientName, address, contactNumber, dentistId, treatmentType, appointmentDate, appointmentTime);

        if (success) {
            out.print("{\"message\": \"Appointment booked successfully\"}");
        } else {
            out.print("{\"error\": \"Failed to book appointment\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String idParam = request.getParameter("id");

        if (idParam == null) {
            out.print("{\"error\": \"Please provide an appointment id, e.g. /appointment?id=1\"}");
            return;
        }

        int appointmentId = Integer.parseInt(idParam);
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        String result = appointmentDAO.getAppointmentById(appointmentId);

        out.print("{\"result\": \"" + result + "\"}");
    }
}