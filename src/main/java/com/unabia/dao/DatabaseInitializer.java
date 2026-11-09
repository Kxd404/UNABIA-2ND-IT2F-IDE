package com.unabia.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Makes sure every table of the approved proposal exists before the menu starts.
 */
public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() throws SQLException {
        String[] tables = {
                // TABLE 1
                "CREATE TABLE IF NOT EXISTS users ("
                        + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username TEXT NOT NULL UNIQUE, "
                        + "password_hash TEXT NOT NULL, "
                        + "full_name TEXT NOT NULL, "
                        + "role TEXT NOT NULL CHECK (role IN ('OWNER', 'CASHIER', 'BARISTA')), "
                        + "status TEXT NOT NULL DEFAULT 'PENDING', "
                        + "created_at TEXT NOT NULL)",
                // TABLE 2
                "CREATE TABLE IF NOT EXISTS products ("
                        + "product_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "product_name TEXT NOT NULL, "
                        + "category TEXT NOT NULL, "
                        + "price REAL NOT NULL, "
                        + "status TEXT NOT NULL DEFAULT 'ACTIVE')",
                // TABLE 3
                "CREATE TABLE IF NOT EXISTS inventory_items ("
                        + "item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "item_name TEXT NOT NULL, "
                        + "unit TEXT NOT NULL, "
                        + "stock_quantity REAL NOT NULL DEFAULT 0, "
                        + "reorder_level REAL NOT NULL DEFAULT 0, "
                        + "status TEXT NOT NULL DEFAULT 'ACTIVE')",
                // TABLE 4
                "CREATE TABLE IF NOT EXISTS product_ingredients ("
                        + "recipe_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "product_id INTEGER NOT NULL REFERENCES products(product_id), "
                        + "item_id INTEGER NOT NULL REFERENCES inventory_items(item_id), "
                        + "quantity_used REAL NOT NULL)",
                // TABLE 5
                "CREATE TABLE IF NOT EXISTS orders ("
                        + "order_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "cashier_id INTEGER NOT NULL REFERENCES users(user_id), "
                        + "order_datetime TEXT NOT NULL, "
                        + "subtotal REAL NOT NULL, "
                        + "discount REAL NOT NULL DEFAULT 0, "
                        + "total_amount REAL NOT NULL, "
                        + "payment_method TEXT NOT NULL, "
                        + "is_void INTEGER NOT NULL DEFAULT 0)",
                // TABLE 6
                "CREATE TABLE IF NOT EXISTS order_items ("
                        + "order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "order_id INTEGER NOT NULL REFERENCES orders(order_id), "
                        + "product_id INTEGER NOT NULL REFERENCES products(product_id), "
                        + "quantity INTEGER NOT NULL, "
                        + "unit_price REAL NOT NULL, "
                        + "line_total REAL NOT NULL)",
                // TABLE 7
                "CREATE TABLE IF NOT EXISTS audit_logs ("
                        + "log_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER REFERENCES users(user_id), "
                        + "action TEXT NOT NULL, "
                        + "table_name TEXT, "
                        + "record_id INTEGER, "
                        + "old_value TEXT, "
                        + "new_value TEXT, "
                        + "log_timestamp TEXT NOT NULL)"
        };

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : tables) {
                stmt.execute(sql);
            }
        }
    }
}
