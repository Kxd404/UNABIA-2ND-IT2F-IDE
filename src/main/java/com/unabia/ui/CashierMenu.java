package com.unabia.ui;

import com.unabia.dao.UserDao;

import java.util.Scanner;

/** Menu for the Cashier role. */
public class CashierMenu extends RoleMenu {

    public CashierMenu(Scanner scanner, UserDao userDao) {
        super(scanner, userDao);
    }

    @Override
    protected String title() {
        return "CASHIER MENU";
    }

    @Override
    protected String[] options() {
        return new String[]{
                "Create New Order",
                "Apply Discount and Print Receipt",
                "View Menu"
        };
    }
}
