<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>School Management System - Error</title>
  <link rel="stylesheet" href="../css/style.css" />
</head>
<body>
  <!-- Error page: shows clear error messages -->
  <header class="site-header">
    <div class="container header-inner">
      <div class="brand">
        <span class="brand-dot" aria-hidden="true"></span>
        <h1>School Management System</h1>
      </div>
    </div>
  </header>

  <main class="container">
    <section class="page">
      <h2 class="page-title">Something went wrong</h2>
      <p class="muted">An error occurred while processing your request.</p>

      <div class="note" style="border-style: solid;">
        <strong>Error:</strong>
        <%= (request.getAttribute("errorMsg") != null) ? request.getAttribute("errorMsg") : "Unknown error" %>
      </div>

      <div class="cta-row" style="margin-top: 1rem;">
        <a class="btn btn-primary" href="dashboard.jsp">Go to Dashboard</a>
        <a class="btn btn-secondary" href="../login.html">Login Page</a>
      </div>
    </section>
  </main>

  <footer class="site-footer">
    <div class="container footer-inner">
      <p>© School Management System 2026</p>
    </div>
  </footer>
</body>
</html>

