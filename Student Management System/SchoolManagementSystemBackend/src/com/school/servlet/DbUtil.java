package com.school.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DbUtil - small JDBC helper used by all servlets.
 *
 * IMPORTANT:
 * - Update DB_USERNAME and DB_PASSWORD.
 * - Ensure MySQL Connector/J is on the Tomcat classpath.
 */
public class DbUtil {

    // TODO: Update these values
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    // Database name: schooldb
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/schooldb?useSSL=false&serverTimezone=UTC";

    static {
        try {
            // Loads com.mysql.cj.jdbc.Driver (MySQL Connector/J 8)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If driver class name differs for your connector version, update accordingly.
            throw new RuntimeException("MySQL JDBC Driver not found. Add MySQL Connector/J to Tomcat.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
    }
}

