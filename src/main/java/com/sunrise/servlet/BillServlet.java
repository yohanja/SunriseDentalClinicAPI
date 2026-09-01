package com.sunrise.servlet;

import com.sunrise.dao.BillDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sunrise.util.DBConnection;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        BillDAO billDAO = new BillDAO();

        if ("markPaid".equals(action)) {
            String billIdParam = request.getParameter("billId");
            if (billIdParam == null) {
                out.print("{\"error\": \"Missing billId\"}");
                return;
            }
            int billId = Integer.parseInt(billIdParam);
            boolean success = billDAO.markAsPaid(billId);

            if (success) {
                out.print("{\"message\": \"Payment recorded successfully\"}");
            } else {
                out.print("{\"error\": \"Failed to update payment status\"}");
            }
            return;
        }

        String appointmentIdParam = request.getParameter("appointmentId");

        if (appointmentIdParam == null) {
            out.print("{\"error\": \"Missing appointmentId\"}");
            return;
        }

        int appointmentId = Integer.parseInt(appointmentIdParam);

        Integer existingBillId = findBillIdByAppointment(appointmentId);

        int billId;
        if (existingBillId != null) {
            billId = existingBillId;
        } else {
            String result = billDAO.generateBill(appointmentId);
            if (result.startsWith("No appointment") || result.startsWith("Error") || result.startsWith("Failed")) {
                out.print("{\"error\": \"" + result + "\"}");
                return;
            }
            Integer newBillId = findBillIdByAppointment(appointmentId);
            if (newBillId == null) {
                out.print("{\"error\": \"Could not retrieve generated bill\"}");
                return;
            }
            billId = newBillId;
        }

        String billJson = billDAO.getBillDetailsJson(billId);
        out.print(billJson);
    }

    private Integer findBillIdByAppointment(int appointmentId) {
        String sql = "SELECT bill_id FROM Bill WHERE appointment_id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("bill_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}