package ru.urfu.mathmechbot.storages.postgresql;

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
    private final PropertiesUtils utils = new PropertiesUtils();

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
                    utils.get(URL),
                    utils.get(USERNAME),
                    utils.get(PASSWORD)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
