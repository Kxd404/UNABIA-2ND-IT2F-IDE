package com.unabia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Opens SQLite connections. Foreign key constraints are switched ON
 * every time a connection is created (SQLite has them OFF by default).
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:coffeeshop.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }
}
