package com.sunrise.servlet;

import com.sunrise.dao.BillDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String appointmentIdParam = request.getParameter("appointmentId");

        if (appointmentIdParam == null) {
            out.print("{\"error\": \"Missing appointmentId\"}");
            return;
        }

        int appointmentId = Integer.parseInt(appointmentIdParam);

        BillDAO billDAO = new BillDAO();
        String result = billDAO.generateBill(appointmentId);

        out.print("{\"result\": \"" + result + "\"}");
    }
}