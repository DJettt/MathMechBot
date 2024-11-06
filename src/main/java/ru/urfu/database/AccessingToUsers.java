package ru.urfu.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessingToUsers {
    private final Logger logger = LoggerFactory.getLogger(AccessingToUserEntry.class);
    private final static String CREATE_USER = "INSERT INTO users (id, current_state) VALUES ?, DEFAULT";
    private final static String UPDATE_CURRENT_STATE = "UPDATE users SET current_state = ? WHERE id = ?";
    private final static String GET_CURRENT_STATE = "SELECT current_state FROM users WHERE id = ?";
    private final static String DELETE_USER = "DELETE FROM users WHERE id = ?";

    private AccessingToUsers() {
    }

    /**
     * Добавляет пользователя в таблицу users.
     * @param id идентификатор пользователя
     */
    public void createUser(Long id) {
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Ошибка при добавлении данных в таблицу.");
            e.printStackTrace();
        }
    }

    /**
     * Изменяет состояние у пользователя в db.
     * @param id идентификатор пользователя
     * @param currentState новое состояние
     */
    public void updateCurrentState(Long id, String currentState) {
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENT_STATE)) {
            preparedStatement.setString(1, currentState);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Ошибка при обновлении данных в таблице.");
            e.printStackTrace();
        }
    }

    /**
     * Получает информацию о текущем состоянии.
     * @param id идентификатор пользователя
     * @return текущее состояние
     */
    public String getCurrentState(Long id) {
        String currentState = null;
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CURRENT_STATE)) {
            preparedStatement.setLong(1, id);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                currentState = results.getString(1);
            }
        }
        catch (SQLException e) {
            logger.error("Ошибка при получении данных из таблицы.");
            e.printStackTrace();
        }
        return currentState;
    }

    /**
     * Удаляет данные из users.
     * @param id идентификатор.
     */
    public void deleteUser(Long id) {
        try(Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            logger.error("Ошибка при удалении данных в таблице.");
            e.printStackTrace();
        }
    }
}
