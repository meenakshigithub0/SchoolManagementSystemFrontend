SchoolManagementSystemBackend Setup (Java Servlets + JSP + MySQL)

This backend project is generated under:

SchoolManagementSystemBackend/

---

## 1) Prerequisites

- Apache Tomcat
- JDK (Java 11+ recommended)
- MySQL Server
- MySQL JDBC driver (Connector/J)

---

## 2) Database Setup (MySQL)

1. Create the database:
   - Database name: `schooldb`

2. Run the schema:
   - `SchoolManagementSystemBackend/database.sql`

This will create:
- `users`
- `students`
- `teachers`
- `contacts`

---

## 3) Project Placement in Tomcat

1. Copy the whole folder:
   - `SchoolManagementSystemBackend` 
   into:
   - `<tomcat>/webapps/`

2. Ensure the final context path matches what you plan to access.
   Example:
   - http://localhost:8080/SchoolManagementSystem/

---

## 4) JDBC Configuration

Open and edit JDBC settings in all servlets (if needed). Typical values:
- JDBC URL: `jdbc:mysql://localhost:3306/schooldb?useSSL=false&serverTimezone=UTC`
- Username: your MySQL username
- Password: your MySQL password

(They are centralized in a helper section inside each servlet in this demo.)

---

## 5) Build / Compile

For a typical Tomcat deployment you can:
- Copy source files into `WEB-INF/classes` by compiling, OR
- Use your IDE/Ant/Maven to compile.

Make sure servlet classes exist at:
- `WEB-INF/classes/com/school/servlet/*`

---

## 6) Access Flow

- Login:
  - GET/POST to `LoginServlet`
- Register:
  - POST to `RegisterServlet`
- Dashboard:
  - JSP: `dashboard.jsp` (invoked after successful login / or directly for demo)

If you wire the frontend, your forms should POST to:
- `/LoginServlet`
- `/RegisterServlet`
- `/ContactServlet`

---

## 7) Notes

- This is a backend demo using JSP + Servlets + JDBC.
- Input validation and error handling are included.
- Passwords are stored as plain text in this sample for simplicity. Use hashing in real projects.

