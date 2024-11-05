package ru.urfu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private final static String PASSWORD = "db.password";
    private final static String USERNAME = "db.username";
    private final static String URL = "db.url";

    private ConnectionManager() {
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtils.get(PASSWORD),
                    PropertiesUtils.get(USERNAME),
                    PropertiesUtils.get(URL)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
