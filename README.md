# Coffee Shop POS and Inventory System
Layered Java + SQLite (DAO architecture) with multi-role authentication.

## Project structure
```
src/main/java/com/unabia/
├── Main.java                    Entry point: initialize DB -> create DAO + Scanner -> Authentication.start()
├── dao/
│   ├── DatabaseConnection.java    SQLite connection, PRAGMA foreign_keys = ON on every connection
│   ├── DatabaseInitializer.java   Creates all 7 tables of the proposal if they do not exist
│   └── UserDao.java               PreparedStatement queries for the users table
├── model/
│   └── User.java                  Java Record
└── ui/
    ├── Authentication.java        Register, Login, and routing by role
    ├── RoleMenu.java              Abstract base of the role menus
    ├── OwnerMenu.java             OWNER menu
    ├── CashierMenu.java           CASHIER menu
    └── BaristaMenu.java           BARISTA menu
src/main/resources/
pom.xml                            Maven setup with sqlite-jdbc
coffeeshop.db                      SQLite database file (created on first run, in the project root)
```

## Roles (from the approved proposal)
OWNER (Owner / Administrator), CASHIER, BARISTA (Barista / Inventory Staff).
New accounts are saved with status PENDING.

## How to run
JDK 17 or higher required. Open the folder containing `pom.xml` in IntelliJ or NetBeans,
let Maven load, then run `com.unabia.Main`.
