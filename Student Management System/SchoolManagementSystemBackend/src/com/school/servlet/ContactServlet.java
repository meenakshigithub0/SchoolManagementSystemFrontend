package com.school.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ContactServlet
 * Handles contact form submission and stores it in `contacts` table.
 */
@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/error.jsp?msg=Use%20POST%20for%20contact%20form");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = trimToNull(request.getParameter("name"));
        String email = trimToNull(request.getParameter("email"));
        String message = trimToNull(request.getParameter("message"));

        if (name == null || email == null || message == null) {
            forwardError(request, response, "All contact fields are required.");
            return;
        }

        // Minimal email check; rely on frontend HTML validation too.
        if (!email.contains("@") || !email.contains(".")) {
            forwardError(request, response, "Please provide a valid email.");
            return;
        }

        String sql = "INSERT INTO contacts(name, email, message) VALUES(?, ?, ?)";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, message);
            ps.executeUpdate();

            request.setAttribute("successMsg", "Thanks for contacting us! Your message was saved.");
            request.getRequestDispatcher("/success.jsp").forward(request, response);

        } catch (SQLException e) {
            forwardError(request, response, "Contact saving failed: " + e.getMessage());
        }
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("errorMsg", msg);
        RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
        rd.forward(request, response);
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

