package com.unabia.ui;

import com.unabia.dao.UserDao;

import java.util.Scanner;

/** Menu for the Owner / Administrator role (full access). */
public class OwnerMenu extends RoleMenu {

    public OwnerMenu(Scanner scanner, UserDao userDao) {
        super(scanner, userDao);
    }

    @Override
    protected String title() {
        return "OWNER / ADMINISTRATOR MENU";
    }

    @Override
    protected String[] options() {
        return new String[]{
                "Manage Users",
                "Manage Menu Products and Prices",
                "Manage Inventory",
                "View Sales Reports",
                "View Audit Logs"
        };
    }
}
