package com.sunrise.servlet;

import com.sunrise.dao.DashboardDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/dashboard-data")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        DashboardDAO dashboardDAO = new DashboardDAO();

        int appointmentCount = dashboardDAO.getAppointmentCount();
        int dentistCount = dashboardDAO.getDentistCount();
        int patientCount = dashboardDAO.getPatientCount();
        String upcomingAppointments = dashboardDAO.getUpcomingAppointmentsJson();

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));

        String json = "{"
                + "\"date\":\"" + today + "\","
                + "\"time\":\"" + time + "\","
                + "\"appointmentCount\":" + appointmentCount + ","
                + "\"dentistCount\":" + dentistCount + ","
                + "\"patientCount\":" + patientCount + ","
                + "\"upcomingAppointments\":" + upcomingAppointments
                + "}";

        out.print(json);
    }
}