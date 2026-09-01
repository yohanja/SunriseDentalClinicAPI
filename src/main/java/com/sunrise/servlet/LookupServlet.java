package com.sunrise.servlet;

import com.sunrise.dao.DentistDAO;
import com.sunrise.dao.PatientDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        } else {
            out.print("{\"error\": \"Invalid lookup type\"}");
        }
    }
}