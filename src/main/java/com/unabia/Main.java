package com.unabia;

import com.unabia.dao.DatabaseInitializer;
import com.unabia.dao.UserDao;
import com.unabia.ui.Authentication;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Application entry point only:
 * initialize database -> create shared DAO and Scanner -> hand over to Authentication.
 */
public class Main {

    public static void main(String[] args) {
        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Cannot initialize the database: " + e.getMessage());
            System.out.println("Make sure the Maven dependencies (sqlite-jdbc) are loaded.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            UserDao userDao = new UserDao();
            new Authentication(scanner, userDao).start();
        }
    }
}
