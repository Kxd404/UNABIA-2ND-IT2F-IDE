package com.unabia.ui;

import com.unabia.dao.UserDao;
import com.unabia.model.User;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Handles the first screens of the app: registration, login, and routing a
 * logged-in user to the menu of their role. It uses the shared Scanner and
 * UserDao it receives; it contains no SQL.
 */
public class Authentication {

    private final Scanner scanner;
    private final UserDao userDao;
    private final OwnerMenu ownerMenu;
    private final CashierMenu cashierMenu;
    private final BaristaMenu baristaMenu;

    public Authentication(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
        // the same Scanner and UserDao instances are shared with every menu
        this.ownerMenu = new OwnerMenu(scanner, userDao);
        this.cashierMenu = new CashierMenu(scanner, userDao);
        this.baristaMenu = new BaristaMenu(scanner, userDao);
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("  COFFEE SHOP POS & INVENTORY SYSTEM");
            System.out.println("=====================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you! Goodbye.");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }

    private void register() {
        System.out.println();
        System.out.println("========== REGISTER ==========");

        try {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            if (username.length() < 3 || username.contains(" ")) {
                System.out.println("Username must be at least 3 characters and have no spaces.");
                return;
            }
            if (userDao.usernameExists(username)) {
                System.out.println("That username is already taken.");
                return;
            }

            System.out.print("Full name: ");
            String fullName = scanner.nextLine().trim();
            if (fullName.isEmpty()) {
                System.out.println("Full name is required.");
                return;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            if (password.length() < 4) {
                System.out.println("Password must be at least 4 characters.");
                return;
            }
            System.out.print("Confirm password: ");
            if (!password.equals(scanner.nextLine().trim())) {
                System.out.println("Passwords do not match.");
                return;
            }

            System.out.println("Select role:");
            System.out.println("  1. OWNER      (Owner / Administrator)");
            System.out.println("  2. CASHIER");
            System.out.println("  3. BARISTA    (Barista / Inventory Staff)");
            System.out.print("Choice: ");
            String role;
            switch (scanner.nextLine().trim()) {
                case "1" -> role = "OWNER";
                case "2" -> role = "CASHIER";
                case "3" -> role = "BARISTA";
                default -> {
                    System.out.println("Invalid role choice.");
                    return;
                }
            }

            userDao.register(username, password, fullName, role);
            System.out.println();
            System.out.println("Account created! Role: " + role + " | Status: PENDING");
            System.out.println("Your account is waiting for approval.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void login() {
        System.out.println();
        System.out.println("============ LOGIN ============");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter both your username and password.");
            return;
        }

        try {
            Optional<User> user = userDao.login(username, password);
            if (user.isEmpty()) {
                System.out.println();
                System.out.println("Wrong username or password. Please check your login details and try again.");
                return;
            }
            routeToRoleMenu(user.get());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /** Sends the logged-in user straight to the menu of their assigned role. */
    private void routeToRoleMenu(User user) {
        switch (user.role()) {
            case "OWNER" -> ownerMenu.show(user);
            case "CASHIER" -> cashierMenu.show(user);
            case "BARISTA" -> baristaMenu.show(user);
            default -> System.out.println("Unknown role: " + user.role());
        }
    }
}
