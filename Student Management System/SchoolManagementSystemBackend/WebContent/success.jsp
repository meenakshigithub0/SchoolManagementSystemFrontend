<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>School Management System - Success</title>
  <link rel="stylesheet" href="../css/style.css" />
</head>
<body>
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
      <h2 class="page-title">Operation successful</h2>
      <p class="muted">Your request completed successfully.</p>

      <div class="note" style="background: rgba(34, 197, 94, 0.10); border-style: solid; border-color: rgba(34, 197, 94, 0.35);">
        <strong>Message:</strong>
        <%= (request.getAttribute("successMsg") != null) ? request.getAttribute("successMsg") : "Done" %>
      </div>

      <div class="cta-row" style="margin-top: 1rem;">
        <a class="btn btn-primary" href="dashboard.jsp">Go to Dashboard</a>
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

