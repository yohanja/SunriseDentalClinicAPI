package com.sunrise.servlet;

import com.sunrise.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String loggedInUsername = (session != null) ? (String) session.getAttribute("username") : null;

        if (loggedInUsername == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"error\": \"Not logged in\"}");
            return;
        }

        String action = request.getParameter("action");
        UserDAO userDAO = new UserDAO();

        if ("allUsers".equals(action)) {
            String role = (String) session.getAttribute("role");
            if (!"Admin".equals(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\": \"Access denied. Admin privileges required.\"}");
                return;
            }
            out.print("{\"users\": " + userDAO.getAllUsersJson() + "}");
        } else {
            out.print(userDAO.getUserProfileJson(loggedInUsername));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String loggedInUsername = (session != null) ? (String) session.getAttribute("username") : null;

        if (loggedInUsername == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"error\": \"Not logged in\"}");
            return;
        }

        String action = request.getParameter("action");
        UserDAO userDAO = new UserDAO();

        if ("changePassword".equals(action)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");

            boolean success = userDAO.changePassword(loggedInUsername, currentPassword, newPassword);

            if (success) {
                out.print("{\"message\": \"Password changed successfully\"}");
            } else {
                out.print("{\"error\": \"Current password is incorrect\"}");
            }

        } else if ("registerStaff".equals(action)) {
            String role = (String) session.getAttribute("role");
            if (!"Admin".equals(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\": \"Access denied. Admin privileges required.\"}");
                return;
            }

            String newUsername = request.getParameter("username");
            String newPassword = request.getParameter("password");
            String newRole = request.getParameter("role");

            if (newUsername == null || newPassword == null || newRole == null) {
                out.print("{\"error\": \"Missing required fields\"}");
                return;
            }

            boolean success = userDAO.registerUser(newUsername, newPassword, newRole);

            if (success) {
                out.print("{\"message\": \"Staff account registered successfully\"}");
            } else {
                out.print("{\"error\": \"Username already exists\"}");
            }

        } else if ("adminResetPassword".equals(action)) {
            String role = (String) session.getAttribute("role");
            if (!"Admin".equals(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"error\": \"Access denied. Admin privileges required.\"}");
                return;
            }

            String targetUsername = request.getParameter("targetUsername");
            String newPassword = request.getParameter("newPassword");

            if (targetUsername == null || newPassword == null) {
                out.print("{\"error\": \"Missing required fields\"}");
                return;
            }

            boolean success = userDAO.adminResetPassword(targetUsername, newPassword);

            if (success) {
                out.print("{\"message\": \"Password reset successfully for " + targetUsername + "\"}");
            } else {
                out.print("{\"error\": \"Failed to reset password\"}");
            }

        } else {
            out.print("{\"error\": \"Invalid action\"}");
        }
    }
}