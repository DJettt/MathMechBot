package ru.urfu.database;

import java.sql.*;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessingToUserEntry {
    private final Logger logger = LoggerFactory.getLogger(AccessingToUserEntry.class);
    private final static String DELETE_INFO = "DELETE FROM user_table WHERE userid = ?";
    private final static String GET_INFO = "SELECT * FROM userentry WHERE userid = ?";
    private final static String UPDATE_INFO = """
    UPDATE userentry SET surname = ?, name = ?, patronym = ?,
            speciality = ?, men = ?, year = ?, group_number = ? WHERE chatid = ?""";
    private final static String SET_INFO = """
            INSERT INTO userentry (surname, name, patronym,
            speciality, men, year, group_number, userid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";

    private AccessingToUserEntry() {
    }

    /**
     * Загружает информацию о пользователе в DB.
     * @param userEntry информация о пользователе.
     */
    public void createUserEntryIntoDb(UserEntry userEntry) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SET_INFO)) {

            preparedStatement.setString(1, userEntry.surname());
            preparedStatement.setString(2, userEntry.name());
            preparedStatement.setString(3, userEntry.patronym());
            preparedStatement.setString(4, userEntry.specialty());
            preparedStatement.setString(5, userEntry.men());
            preparedStatement.setInt(6, userEntry.year());
            preparedStatement.setInt(7, userEntry.group());
            preparedStatement.setLong(8, userEntry.userId());

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Ошибка при добавлении данных в таблицу.");
            e.printStackTrace();
        }
    }

    /**
     * Обновляет информацию о пользователе.
     * @param userEntry обновленная информация.
     */
    public void updateUserEntryIntoDb(UserEntry userEntry) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INFO)) {

            preparedStatement.setString(1, userEntry.surname());
            preparedStatement.setString(2, userEntry.name());
            preparedStatement.setString(3, userEntry.patronym());
            preparedStatement.setString(4, userEntry.specialty());
            preparedStatement.setString(5, userEntry.men());
            preparedStatement.setInt(6, userEntry.year());
            preparedStatement.setInt(7, userEntry.group());
            preparedStatement.setLong(8, userEntry.userId());

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Ошибка при добавлении данных в таблицу.");
            e.printStackTrace();
        }
    }

    /**
     * Получает информацию о пользователе из базы данных.
     * @param chatId идентификатор чата
     * @return объект UserEntry
     */
    public UserEntry getUserEntry(Long chatId) {
        try (Connection connection = ConnectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_INFO)) {
            preparedStatement.setString(1, String.valueOf(chatId));
            ResultSet results = preparedStatement.executeQuery();
            return new UserEntryBuilder(chatId, chatId)
                    .surname(results.getString("surname"))
                    .name(results.getString("name"))
                    .men(results.getString("men"))
                    .group(results.getInt("group_number"))
                    .year(results.getInt("year"))
                    .specialty(results.getString("speciality"))
                    .build();
        } catch (SQLException e) {
            logger.error("Ошибка при получении данных из таблицы.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Удаляет всю информации о пользователе.
     * @param chatId идентификатор пользователя
     */
    public void deleteUserEntryIntoDb(Long chatId) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_INFO)) {
            preparedStatement.setLong(1, chatId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                logger.error("Информация не удалена.");
            }
        }
        catch (Exception e) {
            logger.error("Ошибка при удалении данных из таблицы.");
            e.printStackTrace();
        }
    }
}
