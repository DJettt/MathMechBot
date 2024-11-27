package ru.urfu.mathmechbot.storages.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Создает соединение с базой данных.
 */
public final class ConnectionManager {
    private final static String PASSWORD = "POSTGRES_PASSWORD";
    private final static String USERNAME = "POSTGRES_USER";
    private final static String URL = "POSTGRES_URL";

    /**
     * Конструктор.
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
                    System.getenv(URL),
                    System.getenv(USERNAME),
                    System.getenv(PASSWORD)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
