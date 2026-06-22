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
 * RegisterServlet
 * Handles student/teacher registration:
 * - creates entry in users
 * - creates entry in students or teachers
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/error.jsp?msg=Please%20submit%20registration%20form%20via%20POST");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String name = trimToNull(request.getParameter("name"));
            String ageStr = trimToNull(request.getParameter("age"));
            String className = trimToNull(request.getParameter("class"));
            String role = trimToNull(request.getParameter("role"));
            String password = trimToNull(request.getParameter("password"));

            if (name == null || ageStr == null || className == null || role == null || password == null) {
                forwardError(request, response, "All registration fields are required.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException nfe) {
                forwardError(request, response, "Age must be a valid number.");
                return;
            }

            if (age < 1 || age > 120) {
                forwardError(request, response, "Age must be between 1 and 120.");
                return;
            }

            // For simplicity, use username = name (frontend can pass a username in a real app)
            // If you want a separate username field, update the frontend + DB.
            String username = name.toLowerCase().replaceAll("\\s+", "");

            if (!(role.equalsIgnoreCase("Student") || role.equalsIgnoreCase("Teacher"))) {
                forwardError(request, response, "Role must be Student or Teacher.");
                return;
            }


            String normalizedRole = role.equalsIgnoreCase("Student") ? "Student" : "Teacher";

            String insertUser = "INSERT INTO users(username, password, role) VALUES(?, ?, ?)";
            String insertStudent = "INSERT INTO students(user_id, name, age, class) VALUES(?, ?, ?, ?)";
            String insertTeacher = "INSERT INTO teachers(user_id, name, subject) VALUES(?, ?, ?)";

            // Use transaction so both inserts succeed/fail together
            try (Connection conn = DbUtil.getConnection()) {
                conn.setAutoCommit(false);

                int userId;
                try (PreparedStatement psUser = conn.prepareStatement(insertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    psUser.setString(1, username);
                    psUser.setString(2, password); // demo only
                    psUser.setString(3, normalizedRole);
                    psUser.executeUpdate();

                    try (java.sql.ResultSet keys = psUser.getGeneratedKeys()) {
                        if (keys.next()) {
                            userId = keys.getInt(1);
                        } else {
                            throw new SQLException("Creating user failed: no generated key.");
                        }
                    }
                }

                if (normalizedRole.equals("Student")) {
                    try (PreparedStatement psStu = conn.prepareStatement(insertStudent)) {
                        psStu.setInt(1, userId);
                        psStu.setString(2, name);
                        psStu.setInt(3, age);
                        psStu.setString(4, className);
                        psStu.executeUpdate();
                    }
                } else {
                    // Frontend uses "class" field too; for Teacher we map it to subject.
                    String subject = className;
                    try (PreparedStatement psT = conn.prepareStatement(insertTeacher)) {
                        psT.setInt(1, userId);
                        psT.setString(2, name);
                        psT.setString(3, subject);
                        psT.executeUpdate();
                    }
                }

                conn.commit();
            }

            request.setAttribute("successMsg", "Registration successful! You can now log in.");
            request.getRequestDispatcher("/success.jsp").forward(request, response);

        } catch (SQLException e) {
            forwardError(request, response, "Registration failed: " + e.getMessage());
        }
    }

    private static void forwardError(HttpServletRequest request, HttpServletResponse response, String msg)
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

