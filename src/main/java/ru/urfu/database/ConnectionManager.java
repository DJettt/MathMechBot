package ru.urfu.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Создает соединение с базой данных.
 */
public final class ConnectionManager {
    private final static String PASSWORD = "db.password";
    private final static String USERNAME = "db.username";
    private final static String URL = "db.url";

    /**
     * Конструктор
     */
    public ConnectionManager() {
    }

    /**
     * Подключается к бд.
     * @return объект соединения с бд.
     */
    public Connection open() {
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
