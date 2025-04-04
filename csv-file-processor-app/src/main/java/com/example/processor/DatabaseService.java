package com.example.processor;

import java.sql.*;

public class DatabaseService {

    private static final String JDBC_URL = "jdbc:h2:./data/db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "id IDENTITY PRIMARY KEY, " +
                         "name VARCHAR(100), " +
                         "age INT, " +
                         "email VARCHAR(100))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void insertUser(String name, int age, String email) {
        String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
