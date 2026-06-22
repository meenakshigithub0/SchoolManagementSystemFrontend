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
 * StudentServlet
 * CRUD operations for students.
 * Supported actions via request parameter `action`:
 * - list (default)
 * - add
 * - update
 * - delete
 */
@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {

    public static class Student {
        public int id;
        public String name;
        public int age;
        public String className;

        public Student(int id, String name, int age, String className) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.className = className;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // For demo: allow delete via GET, or redirect to dashboard.
        String action = trimToNull(request.getParameter("action"));
        if (action == null) action = "list";

        try {
            switch (action) {
                case "delete":
                    handleDelete(request, response);
                    break;
                case "list":
                default:
                    forwardDashboardWithStudents(request);
                    break;
            }
        } catch (Exception e) {
            forwardError(request, response, "Student operation failed: " + e.getMessage());
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
                    forwardDashboardWithStudents(request);
                    break;
            }
        } catch (Exception e) {
            forwardError(request, response, "Student operation failed: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void forwardDashboardWithStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Student> students = fetchAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String name = trimToNull(request.getParameter("name"));
        String ageStr = trimToNull(request.getParameter("age"));
        String className = trimToNull(request.getParameter("class"));

        if (name == null || ageStr == null || className == null) {
            forwardError(request, response, "Student name, age, and class are required.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException nfe) {
            forwardError(request, response, "Age must be a valid number.");
            return;
        }

        // In this demo, we create student row with user_id = NULL.
        String sql = "INSERT INTO students(user_id, name, age, class) VALUES(NULL, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, className);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Student added successfully.");
        forwardDashboardWithStudents(request, response);
    }

    private void forwardDashboardWithStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Student> students = fetchAllStudents();
        request.setAttribute("students", students);
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idStr = trimToNull(request.getParameter("id"));
        String name = trimToNull(request.getParameter("name"));
        String ageStr = trimToNull(request.getParameter("age"));
        String className = trimToNull(request.getParameter("class"));

        if (idStr == null || name == null || ageStr == null || className == null) {
            forwardError(request, response, "Update requires id, name, age, and class.");
            return;
        }

        int id;
        int age;
        try {
            id = Integer.parseInt(idStr);
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException nfe) {
            forwardError(request, response, "ID and age must be numbers.");
            return;
        }

        String sql = "UPDATE students SET name = ?, age = ?, class = ? WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, className);
            ps.setInt(4, id);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Student updated successfully.");
        forwardDashboardWithStudents(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String idStr = trimToNull(request.getParameter("id"));
        if (idStr == null) {
            forwardError(request, response, "Delete requires student id.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException nfe) {
            forwardError(request, response, "Invalid student id.");
            return;
        }

        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }

        request.setAttribute("successMsg", "Student deleted successfully.");
        forwardDashboardWithStudents(request, response);
    }

    private List<Student> fetchAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, age, class FROM students ORDER BY id DESC";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("class")
                ));
            }
        }
        return list;
    }

    private static void forwardError(HttpServletRequest request, HttpServletResponse response, String msg)
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

