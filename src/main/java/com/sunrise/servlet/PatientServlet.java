package com.sunrise.servlet;

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
        PatientDAO patientDAO = new PatientDAO();

        if (idParam != null) {
            int patientId = Integer.parseInt(idParam);
            String result = patientDAO.getPatientById(patientId);
            out.print("{\"result\": \"" + result + "\"}");
        } else {
            out.print("{\"error\": \"Please provide a patient id, e.g. /patient?id=1\"}");
        }
    }
}