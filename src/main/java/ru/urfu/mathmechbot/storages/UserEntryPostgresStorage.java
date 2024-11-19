package ru.urfu.mathmechbot.storages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.postgresql.ConnectionManager;

/**
 * Обеспечивает коннект с таблицей userentry в БД.
 */
public class UserEntryPostgresStorage implements UserEntryStorage {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Logger logger = LoggerFactory.getLogger(UserEntryPostgresStorage.class);
    private final static String LOG_ERROR_UNTIL_UPDATE = "Информация не обновлена.";
    //TODO было бы классно выделить всю структуру таблицы в отдельный файл, возможно properties
    private final static String DELETE_INFO = "DELETE FROM userentry WHERE userid = ?";
    private final static String GET_INFO = "SELECT * FROM userentry WHERE userid = ?";
    private final static String GEL_ALL_INFO = "SELECT * FROM userentry";
    private final static String UPDATE_INFO = """
            UPDATE userentry SET surname = ?, name = ?, patronym = ?,
            speciality = ?, men = ?, year = ?, group_number = ? WHERE userid = ?""";
    private final static String SET_INFO = """
            INSERT INTO userentry (surname, name, patronym,
            speciality, men, year, group_number, userid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";
    private final static String TABLE_NAME = "userentry";
    private final static String CREATE_TABLE = """
            CREATE TABLE %s (
            id SERIAL PRIMARY KEY,
            surname VARCHAR(50),
            name VARCHAR(50),
            patronym VARCHAR(50),
            speciality VARCHAR(10),
            men VARCHAR(20),
            year INT,
            group_number INT,
            userid BIGINT
            )""";
    private final static String EXIST_CHECK = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
    private final static String CHANGE_NAME = "UPDATE userentry SET name = ? WHERE userid = ?";
    private final static String CHANGE_SURNAME = "UPDATE userentry SET surname = ? WHERE userid = ?";
    private final static String CHANGE_PATRONYM = "UPDATE userentry SET patronym = ? WHERE userid = ?";
    private final static String CHANGE_YEAR = "UPDATE userentry SET year = ? WHERE userid = ?";
    private final static String CHANGE_SPECIALITY = "UPDATE userentry SET speciality = ? WHERE userid = ?";
    private final static String CHANGE_GROUP = "UPDATE userentry SET group_number = ? WHERE userid = ?";
    private final static String CHANGE_MEN = "UPDATE userentry SET men = ? WHERE userid = ?";
    private final static int SET_FIRST = 1;
    private final static int SET_SECOND = 2;
    private final static int SET_THIRD = 3;
    private final static int SET_FOURTH = 4;
    private final static int SET_FIFTH = 5;
    private final static int SET_SIXTH = 6;
    private final static int SET_SEVENTH = 7;
    private final static int SET_EIGHTH = 8;

    /**
     * Конструктор. Создает таблицу в БД если ее не существует.
     */
    public UserEntryPostgresStorage() {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_CHECK)) {
            preparedStatement.setString(SET_FIRST, TABLE_NAME);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            int count = result.getInt(1);
            if (count == 0) {
                try (PreparedStatement createStatement = connection.prepareStatement(
                        String.format(CREATE_TABLE, TABLE_NAME))) {
                    createStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logger.error("При создании таблицы что-то пошло не так...");
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public Optional<ru.urfu.mathmechbot.models.UserEntry> get(Long id) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_INFO)) {
            preparedStatement.setLong(SET_FIRST, id);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                return Optional.of(new UserEntryBuilder(id, id)
                        .surname(results.getString("surname"))
                        .name(results.getString("name"))
                        .men(results.getString("men"))
                        .group(results.getInt("group_number"))
                        .year(results.getInt("year"))
                        .specialty(results.getString("speciality"))
                        .build());
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении данных из таблицы.");
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public List<UserEntry> getAll() {
        List<UserEntry> listUserEntry = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GEL_ALL_INFO)) {
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                listUserEntry.add(new UserEntryBuilder(results.getLong("userid"), results.getLong("userid"))
                        .name(results.getString("name"))
                        .surname(results.getString("surname"))
                        .patronym(results.getString("patronym"))
                        .specialty(results.getString("speciality"))
                        .year(results.getInt("year"))
                        .group(results.getInt("group_number"))
                        .men(results.getString("men"))
                        .build());
            }
            return listUserEntry;
        } catch (SQLException e) {
            logger.error("Ошибка при получении всех данных из таблицы.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(UserEntry userEntry) throws IllegalArgumentException {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SET_INFO)) {
            preparedStatement.setString(SET_FIRST, userEntry.surname());
            preparedStatement.setString(SET_SECOND, userEntry.name());
            preparedStatement.setString(SET_THIRD, userEntry.patronym());
            preparedStatement.setString(SET_FOURTH, userEntry.specialty());
            preparedStatement.setString(SET_FIFTH, userEntry.men());
            if (userEntry.year() != null) {
                preparedStatement.setInt(SET_SIXTH, userEntry.year());
            } else {
                preparedStatement.setInt(SET_SIXTH, 0);
            }
            if (userEntry.group() != null) {
                preparedStatement.setInt(SET_SEVENTH, userEntry.group());
            } else {
                preparedStatement.setInt(SET_SEVENTH, 0);
            }
            preparedStatement.setLong(SET_EIGHTH, userEntry.userId());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Ошибка при добавлении данных в таблицу.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UserEntry userEntry) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INFO)) {

            preparedStatement.setString(SET_FIRST, userEntry.surname());
            preparedStatement.setString(SET_SECOND, userEntry.name());
            preparedStatement.setString(SET_THIRD, userEntry.patronym());
            preparedStatement.setString(SET_FOURTH, userEntry.specialty());
            preparedStatement.setString(SET_FIFTH, userEntry.men());
            if (userEntry.year() != null) {
                preparedStatement.setInt(SET_SIXTH, userEntry.year());
            } else {
                preparedStatement.setInt(SET_SIXTH, 0);
            }
            if (userEntry.group() != null) {
                preparedStatement.setInt(SET_SEVENTH, userEntry.group());
            } else {
                preparedStatement.setInt(SET_SEVENTH, 0);
            }
            preparedStatement.setLong(SET_EIGHTH, userEntry.userId());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Ошибка при обновлении данных в таблице.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UserEntry userEntry) {
        Long userId = userEntry.id();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_INFO)) {
            preparedStatement.setLong(SET_FIRST, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                logger.error("Информация не удалена.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении данных из таблицы.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_NAME)) {
            preparedStatement.setString(SET_FIRST, name);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении имени пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_SURNAME)) {
            preparedStatement.setString(SET_FIRST, surname);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении фамилии пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_PATRONYM)) {
            preparedStatement.setString(SET_FIRST, patronym);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении отчества пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_YEAR)) {
            preparedStatement.setInt(SET_FIRST, year);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении курса пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_SPECIALITY)) {
            preparedStatement.setString(SET_FIRST, specialty);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении направления пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_MEN)) {
            preparedStatement.setString(SET_FIRST, men);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении МЕН пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_GROUP)) {
            preparedStatement.setInt(SET_FIRST, group);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.error(LOG_ERROR_UNTIL_UPDATE);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении группы пользователя.");
            throw new RuntimeException(e);
        }
    }
}
