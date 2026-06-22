SchoolManagementSystemBackend (Java Servlets + JSP + MySQL)

## 1) Project Layout
- `src/com/school/servlet/*.java` (Servlets)
- `WebContent/*.jsp` (JSP pages)
- `WEB-INF/web.xml` (Servlet mappings)
- `database.sql` (MySQL schema)

> Note: The JSP pages reference `../css/style.css` (your existing frontend CSS). When deploying, ensure CSS is reachable.

## 2) MySQL Setup
1. Start MySQL.
2. Run `database.sql`:
   - Database: `schooldb`
   - Tables: `users`, `students`, `teachers`, `contacts`

## 3) JDBC Configuration
Edit `src/com/school/servlet/DbUtil.java`:
- `DB_USERNAME`
- `DB_PASSWORD`
- `JDBC_URL`

Ensure MySQL Connector/J is available to Tomcat.

## 4) Build/Compile for Tomcat
Compile Java sources into `WEB-INF/classes`.

Example (conceptual):
- Compile `src/` with servlet + JDBC dependencies on the classpath.
- Copy compiled `.class` files into `SchoolManagementSystemBackend/WEB-INF/classes/com/school/servlet/`

## 5) Deploy to Tomcat
1. Copy `SchoolManagementSystemBackend/` into:
   - `<tomcat>/webapps/SchoolManagementSystemBackend/`
2. Start Tomcat.

## 6) Access
- Dashboard (example after login):
  - `http://localhost:8080/SchoolManagementSystemBackend/dashboard.jsp`

- Servlets:
  - `/LoginServlet`
  - `/RegisterServlet`
  - `/StudentServlet`
  - `/TeacherServlet`
  - `/ContactServlet`

## 7) Notes / Demo Behavior
- Passwords are stored as plain text for demo purposes only.
- `RegisterServlet` uses `username = name (lowercased)` for the `users` table.
- Dashboard includes simple CRUD forms for Students/Teachers.

