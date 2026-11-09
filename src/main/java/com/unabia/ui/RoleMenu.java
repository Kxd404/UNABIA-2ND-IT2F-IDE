package com.unabia.ui;

import com.unabia.dao.UserDao;
import com.unabia.model.User;

import java.util.Scanner;

/**
 * Base class of every role-based menu. Subclasses only supply the title and options.
 * The shared Scanner and UserDao are passed in, never created here.
 */
public abstract class RoleMenu {

    protected final Scanner scanner;
    protected final UserDao userDao;

    protected RoleMenu(Scanner scanner, UserDao userDao) {
        this.scanner = scanner;
        this.userDao = userDao;
    }

    protected abstract String title();

    protected abstract String[] options();

    /** Runs the menu until the user logs out. */
    public void show(User user) {
        String[] options = options();
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("  " + title());
            System.out.println("=====================================");
            System.out.println("Welcome, " + user.fullName() + "  |  Role: " + user.role()
                    + "  |  Status: " + user.status());
            if (user.status().equals("PENDING")) {
                System.out.println("Note: your account is still pending approval.");
            }
            System.out.println("-------------------------------------");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.println("0. Logout");

            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                loggedIn = false;
                System.out.println("You have been logged out.");
            } else if (isValidOption(choice, options.length)) {
                int index = Integer.parseInt(choice) - 1;
                System.out.println(options[index] + " - this module will be added in a later deliverable.");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private boolean isValidOption(String choice, int count) {
        try {
            int n = Integer.parseInt(choice);
            return n >= 1 && n <= count;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
