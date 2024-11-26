package ru.urfu.mathmechbot.storages;

import java.sql.*;
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
 * Обеспечивает коннект с таблицей userentries в БД.
 */
public class UserEntryPostgresStorage implements UserEntryStorage {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Logger logger = LoggerFactory.getLogger(UserEntryPostgresStorage.class);
    private final static String LOG_INFORMATION_UPDATE_FAILED = "Информация не обновлена.";
    private final static String DELETE_INFO_QUERY = "DELETE FROM userentries WHERE userid = ?";
    private final static String GET_INFO_QUERY = "SELECT * FROM userentries WHERE userid = ?";
    private final static String GEL_ALL_INFO_QUERY = "SELECT * FROM userentries";
    private final static String UPDATE_INFO_QUERY = """
            UPDATE userentries SET surname = ?, name = ?, patronym = ?,
            speciality = ?, men = ?, year = ?, group_number = ? WHERE userid = ?""";
    private final static String SET_INFO_QUERY = """
            INSERT INTO userentries (surname, name, patronym,
            speciality, men, year, group_number, userid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";
    private final static String CREATE_TABLE_IF_NOT_EXIST_QUERY = """
            CREATE TABLE IF NOT EXISTS userentries (
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
    private final static String CHANGE_NAME_QUERY = "UPDATE userentries SET name = ? WHERE userid = ?";
    private final static String CHANGE_SURNAME_QUERY = "UPDATE userentries SET surname = ? WHERE userid = ?";
    private final static String CHANGE_PATRONYM_QUERY = "UPDATE userentries SET patronym = ? WHERE userid = ?";
    private final static String CHANGE_YEAR_QUERY = "UPDATE userentries SET year = ? WHERE userid = ?";
    private final static String CHANGE_SPECIALITY_QUERY = "UPDATE userentries SET speciality = ? WHERE userid = ?";
    private final static String CHANGE_GROUP_QUERY = "UPDATE userentries SET group_number = ? WHERE userid = ?";
    private final static String CHANGE_MEN_QUERY = "UPDATE userentries SET men = ? WHERE userid = ?";
    private final static String USERID_STRING = "userid";
    private final static String NAME_STRING = "name";
    private final static String SURNAME_STRING = "surname";
    private final static String PATRONYM_STRING = "patronym";
    private final static String SPECIALITY_STRING = "speciality";
    private final static String YEAR_STRING = "year";
    private final static String GROUP_NUMBER_STRING = "group_number";
    private final static String MEN_STRING = "men";
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
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_IF_NOT_EXIST_QUERY)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("При создании таблицы что-то пошло не так...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает UserEntry из данных, полученных при SQL запросе.
     * @param result данный из базы данных.
     * @return объект UserEntry
     */
    private UserEntry createUserEntryFromResultSet(ResultSet result) throws SQLException {
        return new UserEntryBuilder(result.getLong(USERID_STRING), result.getLong(USERID_STRING))
                .name(result.getString(NAME_STRING))
                .surname(result.getString(SURNAME_STRING))
                .patronym(result.getString(PATRONYM_STRING))
                .specialty(result.getString(SPECIALITY_STRING))
                .year(result.getInt(YEAR_STRING))
                .group(result.getInt(GROUP_NUMBER_STRING))
                .men(result.getString(MEN_STRING))
                .build();
    }

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public Optional<UserEntry> get(Long id) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_INFO_QUERY)) {
            preparedStatement.setLong(SET_FIRST, id);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                return Optional.of(createUserEntryFromResultSet(results));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении данных из таблицы.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntry> getAll() {
        List<UserEntry> listUserEntry = new ArrayList<>();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(GEL_ALL_INFO_QUERY)) {
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                listUserEntry.add(createUserEntryFromResultSet(results));
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
             PreparedStatement preparedStatement = connection.prepareStatement(SET_INFO_QUERY)) {
            preparedStatement.setString(SET_FIRST, userEntry.surname());
            preparedStatement.setString(SET_SECOND, userEntry.name());
            preparedStatement.setString(SET_THIRD, userEntry.patronym());
            preparedStatement.setString(SET_FOURTH, userEntry.specialty());
            preparedStatement.setString(SET_FIFTH, userEntry.men());
            if (userEntry.year() != null) {
                preparedStatement.setInt(SET_SIXTH, userEntry.year());
            } else {
                preparedStatement.setNull(SET_SIXTH, Types.INTEGER);
            }
            if (userEntry.group() != null) {
                preparedStatement.setInt(SET_SEVENTH, userEntry.group());
            } else {
                preparedStatement.setNull(SET_SEVENTH, Types.INTEGER);
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
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INFO_QUERY)) {

            preparedStatement.setString(SET_FIRST, userEntry.surname());
            preparedStatement.setString(SET_SECOND, userEntry.name());
            preparedStatement.setString(SET_THIRD, userEntry.patronym());
            preparedStatement.setString(SET_FOURTH, userEntry.specialty());
            preparedStatement.setString(SET_FIFTH, userEntry.men());
            if (userEntry.year() != null) {
                preparedStatement.setInt(SET_SIXTH, userEntry.year());
            } else {
                preparedStatement.setNull(SET_SIXTH, Types.INTEGER);
            }
            if (userEntry.group() != null) {
                preparedStatement.setInt(SET_SEVENTH, userEntry.group());
            } else {
                preparedStatement.setNull(SET_SEVENTH, Types.INTEGER);
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
        long userId = userEntry.id();
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_INFO_QUERY)) {
            preparedStatement.setLong(SET_FIRST, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected <= 0) {
                logger.warn("Информация не удалена.");
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении данных из таблицы.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_NAME_QUERY)) {
            preparedStatement.setString(SET_FIRST, name);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении имени пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_SURNAME_QUERY)) {
            preparedStatement.setString(SET_FIRST, surname);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении фамилии пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_PATRONYM_QUERY)) {
            preparedStatement.setString(SET_FIRST, patronym);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении отчества пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_YEAR_QUERY)) {
            preparedStatement.setInt(SET_FIRST, year);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении курса пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_SPECIALITY_QUERY)) {
            preparedStatement.setString(SET_FIRST, specialty);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении направления пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_MEN_QUERY)) {
            preparedStatement.setString(SET_FIRST, men);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении МЕН пользователя.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        try (Connection connection = connectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_GROUP_QUERY)) {
            preparedStatement.setInt(SET_FIRST, group);
            preparedStatement.setLong(SET_SECOND, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected != 1) {
                logger.warn(LOG_INFORMATION_UPDATE_FAILED);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при изменении группы пользователя.");
            throw new RuntimeException(e);
        }
    }
}
