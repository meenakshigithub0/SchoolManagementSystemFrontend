<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.school.servlet.StudentServlet.Student" %>
<%@ page import="com.school.servlet.TeacherServlet.Teacher" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>School Management System - Dashboard</title>
  <link rel="stylesheet" href="../css/style.css" />
</head>
<body>
  <header class="site-header">
    <div class="container header-inner">
      <div class="brand">
        <span class="brand-dot" aria-hidden="true"></span>
        <h1>School Management System</h1>
      </div>

      <nav class="nav" aria-label="Primary navigation">
        <a class="nav-link" href="dashboard.jsp">Dashboard</a>
        <a class="nav-link" href="../login.html">Login</a>
        <a class="nav-link" href="../register.html">Register</a>
      </nav>
    </div>
  </header>

  <main class="container">
    <section class="page">
      <div class="page-header page-header-row">
        <div>
          <h2 class="page-title">Dashboard</h2>
          <p class="muted">Students & Teachers tables with CRUD actions (demo).</p>
        </div>
      </div>

      <!-- Messages -->
      <%
        String successMsg = (String) request.getAttribute("successMsg");
        String errorMsg = (String) request.getAttribute("errorMsg");
      %>
      <%
        if (successMsg != null) {
      %>
        <div class="note" style="background: rgba(34, 197, 94, 0.10); border-style: solid; border-color: rgba(34, 197, 94, 0.35);">
          <strong>Success:</strong> <%= successMsg %>
        </div>
      <%
        }
        if (errorMsg != null) {
      %>
        <div class="note" style="background: rgba(239, 68, 68, 0.08); border-style: solid; border-color: rgba(239, 68, 68, 0.25);">
          <strong>Error:</strong> <%= errorMsg %>
        </div>
      <%
        }
      %>

      <!-- Students CRUD -->
      <section class="dash-section" style="margin-top: 1.8rem;">
        <h3 class="section-title">Students</h3>

        <form class="form" method="post" action="StudentServlet" style="margin-bottom: 1rem;">
          <input type="hidden" name="action" value="add" />
          <div class="form-group" style="flex-direction: row; gap: 1rem;">
            <div style="flex:1;">
              <label>Name <span aria-hidden="true">*</span></label>
              <input class="input" type="text" name="name" required minlength="2" placeholder="Student name" />
            </div>
            <div style="width: 150px;">
              <label>Age <span aria-hidden="true">*</span></label>
              <input class="input" type="number" name="age" required min="1" max="120" placeholder="Age" />
            </div>
            <div style="flex:1;">
              <label>Class <span aria-hidden="true">*</span></label>
              <input class="input" type="text" name="class" required placeholder="Class 6" />
            </div>
          </div>
          <button class="btn btn-primary" type="submit">Add Student</button>
        </form>

        <div class="table-wrap" role="region" aria-label="Students">
          <table class="table">
            <thead>
              <tr>
                <th>Student ID</th>
                <th>Name</th>
                <th>Class</th>
                <th>Age</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <%
                List<Student> students = (List<Student>) request.getAttribute("students");
                if (students != null) {
                  for (Student s : students) {
              %>
                <tr>
                  <td><%= s.id %></td>
                  <td><%= s.name %></td>
                  <td><%= s.className %></td>
                  <td><%= s.age %></td>
                  <td>
                    <!-- Update (simple inline form) -->
                    <form method="post" action="StudentServlet" style="display:inline-block; margin-right:0.5rem;">
                      <input type="hidden" name="action" value="update" />
                      <input type="hidden" name="id" value="<%= s.id %>" />
                      <input class="input" style="width: 150px; padding: 0.45rem 0.6rem; border-radius: 10px;" type="text" name="name" value="<%= s.name %>" required />
                      <input class="input" style="width: 90px; padding: 0.45rem 0.6rem; border-radius: 10px; margin-left:0.4rem;" type="number" name="age" value="<%= s.age %>" required />
                      <input class="input" style="width: 110px; padding: 0.45rem 0.6rem; border-radius: 10px; margin-left:0.4rem;" type="text" name="class" value="<%= s.className %>" required />
                      <button class="btn btn-secondary" type="submit" style="margin-left:0.4rem; padding:0.45rem 0.75rem; font-size:0.9rem;">Update</button>
                    </form>

                    <form method="get" action="StudentServlet" style="display:inline-block;">
                      <input type="hidden" name="action" value="delete" />
                      <input type="hidden" name="id" value="<%= s.id %>" />
                      <button class="btn btn-secondary" type="submit" style="background: rgba(239, 68, 68, 0.18); border-color: rgba(239, 68, 68, 0.35);">Delete</button>
                    </form>
                  </td>
                </tr>
              <%
                  }
                } else {
              %>
                <tr><td colspan="5" style="padding: 1rem; color: rgba(234,240,255,0.75);">No student records found.</td></tr>
              <%
                }
              %>
            </tbody>
          </table>
        </div>
      </section>

      <!-- Teachers CRUD -->
      <section class="dash-section" style="margin-top: 1.8rem;">
        <h3 class="section-title">Teachers</h3>

        <form class="form" method="post" action="TeacherServlet" style="margin-bottom: 1rem;">
          <input type="hidden" name="action" value="add" />
          <div class="form-group" style="flex-direction: row; gap: 1rem;">
            <div style="flex:1;">
              <label>Name <span aria-hidden="true">*</span></label>
              <input class="input" type="text" name="name" required minlength="2" placeholder="Teacher name" />
            </div>
            <div style="flex:1;">
              <label>Subject <span aria-hidden="true">*</span></label>
              <input class="input" type="text" name="subject" required placeholder="Mathematics" />
            </div>
          </div>
          <button class="btn btn-primary" type="submit">Add Teacher</button>
        </form>

        <div class="table-wrap" role="region" aria-label="Teachers">
          <table class="table">
            <thead>
              <tr>
                <th>Teacher ID</th>
                <th>Name</th>
                <th>Subject</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <%
                List<Teacher> teachers = (List<Teacher>) request.getAttribute("teachers");
                if (teachers != null) {
                  for (Teacher t : teachers) {
              %>
                <tr>
                  <td><%= t.id %></td>
                  <td><%= t.name %></td>
                  <td><%= t.subject %></td>
                  <td>
                    <form method="post" action="TeacherServlet" style="display:inline-block; margin-right:0.5rem;">
                      <input type="hidden" name="action" value="update" />
                      <input type="hidden" name="id" value="<%= t.id %>" />
                      <input class="input" style="width: 170px; padding: 0.45rem 0.6rem; border-radius: 10px;" type="text" name="name" value="<%= t.name %>" required />
                      <input class="input" style="width: 170px; padding: 0.45rem 0.6rem; border-radius: 10px; margin-left:0.4rem;" type="text" name="subject" value="<%= t.subject %>" required />
                      <button class="btn btn-secondary" type="submit" style="margin-left:0.4rem; padding:0.45rem 0.75rem; font-size:0.9rem;">Update</button>
                    </form>

                    <form method="get" action="TeacherServlet" style="display:inline-block;">
                      <input type="hidden" name="action" value="delete" />
                      <input type="hidden" name="id" value="<%= t.id %>" />
                      <button class="btn btn-secondary" type="submit" style="background: rgba(239, 68, 68, 0.18); border-color: rgba(239, 68, 68, 0.35);">Delete</button>
                    </form>
                  </td>
                </tr>
              <%
                  }
                } else {
              %>
                <tr><td colspan="4" style="padding: 1rem; color: rgba(234,240,255,0.75);">No teacher records found.</td></tr>
              <%
                }
              %>
            </tbody>
          </table>
        </div>
      </section>

    </section>
  </main>

  <footer class="site-footer">
    <div class="container footer-inner">
      <p>© School Management System 2026</p>
    </div>
  </footer>
</body>
</html>

