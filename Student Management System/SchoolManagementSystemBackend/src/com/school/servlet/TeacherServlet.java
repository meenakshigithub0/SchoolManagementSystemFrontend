package com.school.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TeacherServlet
 * CRUD operations for teachers.
 * Supported actions via request parameter `action`:
 * - list (default)
 * - add
 * - update
 * - delete
 */
@WebServlet("/TeacherServlet")
public class TeacherServlet extends HttpServlet {

    public static class Teacher {
        public int id;
        public String name;
        public String subject;

        public Teacher(int id, String name, String subject) {
            this.id = id;
            this.name = name;
            this.subject = subject;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = trimToNull(request.getParameter("action"));
        if (action == null) action = "list";

        try {
            switch (action) {
                case "delete":
                    handleDelete(request, response);
                    break;
                case "list":
                default:
                    forwardDashboardWithTeachers(request);
                    break;
            }
        } catch (Exception e) {
            forwardError(request, response, "Teacher operation failed: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = trimToNull(request.getParameter("action"));
        if (action == null) action = "add";

        try {
            switch (action) {
                case "add":
                    handleAdd(request, response);
                    break;
                case "update":
                    handleUpdate(request, response);
                    break;
                default:
                    forwardDashboardWithTeachers(request);
                    break;
            }
        } catch (Exception e) {
            forwardError(request, response, "Teacher operation failed: " + e.getMessage());
        }
    }

    private void forwardDashboardWithTeachers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Teacher> teachers = fetchAllTeachers();
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String name = trimToNull(request.getParameter("name"));
        String subject = trimToNull(request.getParameter("subject"));

        if (name == null || subject == null) {
            forwardError(request, response, "Teacher name and subject are required.");
            return;
        }

        String sql = "INSERT INTO teachers(user_id, name, subject) VALUES(NULL, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, subject);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Teacher added successfully.");
        forwardDashboardWithTeachers(request, response);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idStr = trimToNull(request.getParameter("id"));
        String name = trimToNull(request.getParameter("name"));
        String subject = trimToNull(request.getParameter("subject"));

        if (idStr == null || name == null || subject == null) {
            forwardError(request, response, "Update requires id, name, and subject.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException nfe) {
            forwardError(request, response, "Invalid teacher id.");
            return;
        }

        String sql = "UPDATE teachers SET name = ?, subject = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, subject);
            ps.setInt(3, id);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Teacher updated successfully.");
        forwardDashboardWithTeachers(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idStr = trimToNull(request.getParameter("id"));
        if (idStr == null) {
            forwardError(request, response, "Delete requires teacher id.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException nfe) {
            forwardError(request, response, "Invalid teacher id.");
            return;
        }

        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Teacher deleted successfully.");
        forwardDashboardWithTeachers(request, response);
    }

    private List<Teacher> fetchAllTeachers() throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT id, name, subject FROM teachers ORDER BY id DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("subject")
                ));
            }
        }
        return list;
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        request.setAttribute("errorMsg", msg);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

