package ru.urfu.mathmechbot.storages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.converter.CurrentStateConverter;
import ru.urfu.mathmechbot.storages.postgresql.ConnectionManager;

/**
 * Соединение с таблицей users в БД.
 */
public class UserPostgresStorage implements UserStorage {
    private final CurrentStateConverter converter = new CurrentStateConverter();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Logger logger = LoggerFactory.getLogger(UserPostgresStorage.class);
    private final static String TABLE_NAME = "users";
    private final static String CREATE_TABLE = """
            CREATE TABLE %s (
            userid BIGINT,
            current_state VARCHAR(50)
            )""";
    private final static String EXIST_CHECK = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
    private final static String CURRENT_STATE_STRING = "current_state";
    private final static String CREATE_USER = "INSERT INTO users (id, current_state) VALUES (?, ?);";
    private final static String UPDATE_CURRENT_STATE = "UPDATE users SET current_state = ? WHERE id = ?";
    private final static String GET_CURRENT_STATE = "SELECT current_state FROM users WHERE id = ?";
    private final static String GET_ALL = "SELECT * FROM users";
    private final static String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private final static int SET_FIRST = 1;
    private final static int SET_SECOND = 2;

    /**
     * Конструктор. Создает таблицу в БД если ее не существует.
     */
    public UserPostgresStorage() {
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
        }
    }

    @Override
    public void add(User user) {
        Long id = user.id();
        String currentState = converter.convert(user.currentState());
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
            preparedStatement.setLong(SET_FIRST, id);
            preparedStatement.setString(SET_SECOND, currentState);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при добавлении данных в таблицу.");
        }
    }

    @Override
    public void changeUserState(@NotNull Long id, @NotNull UserState state) {
        String currentState = converter.convert(state);
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENT_STATE)) {
            preparedStatement.setString(SET_FIRST, currentState);
            preparedStatement.setLong(SET_SECOND, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при изменении состояния.");
        }
    }

    @Override
    public void update(User user) {
        String currentState = converter.convert(user.currentState());
        Long id = user.id();
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CURRENT_STATE)) {
            preparedStatement.setString(SET_FIRST, currentState);
            preparedStatement.setLong(SET_SECOND, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении данных в таблице.");
        }
    }

    @Override
    public Optional<User> get(Long id) {
        String currentState = null;
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CURRENT_STATE)) {
            preparedStatement.setLong(SET_FIRST, id);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                currentState = results.getString(CURRENT_STATE_STRING);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении данных из таблицы.");
        }
        if (converter.convert(currentState) == null) {
            User newUser = new User(id, UserState.DEFAULT);
            this.add(newUser);
            return Optional.of(newUser);
        }
        return Optional.of(new User(id, Objects.requireNonNull(converter.convert(currentState))));
    }

    @Override
    public List<User> getAll() {
        List<User> newList = new ArrayList<User>();
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
            ResultSet result = preparedStatement.getResultSet();
            while (result.next()) {
                newList.add(new User(result.getLong("id"),
                        Objects.requireNonNull(converter.convert(result.getString(CURRENT_STATE_STRING)))));
            }
            return newList;
        } catch (SQLException e) {
            logger.error("Ошибка при получении всех данных из таблицы.");
            return null;
        }
    }

    @Override
    public void delete(User user) {
        Long id = user.id();
        try (Connection connection = connectionManager.open();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(SET_FIRST, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при удалении данных в таблице.");
        }
    }
}
