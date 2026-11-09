package com.unabia.ui;

import com.unabia.dao.UserDao;

import java.util.Scanner;

/** Menu for the Barista / Inventory Staff role. */
public class BaristaMenu extends RoleMenu {

    public BaristaMenu(Scanner scanner, UserDao userDao) {
        super(scanner, userDao);
    }

    @Override
    protected String title() {
        return "BARISTA / INVENTORY STAFF MENU";
    }

    @Override
    protected String[] options() {
        return new String[]{
                "View Incoming Orders",
                "Record Ingredient Restock",
                "Adjust Stock Levels",
                "View Inventory Report"
        };
    }
}
