package com.sunrise.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String helpText = "Sunrise Dental Clinic System - Help Guide. "
                + "1. Login using your staff username and password. "
                + "2. Use /patient?id=X to view patient details. "
                + "3. Use /appointment (POST) to register a new appointment. "
                + "4. Use /appointment?id=X (GET) to view appointment details. "
                + "5. Use /bill (POST) with an appointmentId to generate a bill.";

        out.print("{\"help\": \"" + helpText + "\"}");
    }
}