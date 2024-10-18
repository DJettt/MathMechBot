package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Структура, содержащая все хранилища для MathMechBot.</p>
 */
public final class MathMechStorage {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * <p>Конструктор.</p>
     *
     * @param userStorage      хранилище User.
     * @param userEntryStorage хранилище UserEntry.
     */
    public MathMechStorage(
            @NotNull UserStorage userStorage,
            @NotNull UserEntryStorage userEntryStorage) {
        this.users = userStorage;
        this.userEntries = userEntryStorage;
    }

    /**
     * <p>Геттер хранилища объектов User.</p>
     *
     * @return хранилище объектов User.
     */
    public UserStorage getUsers() {
        return users;
    }

    /**
     * <p>Геттер хранилища объектов UserEntry.</p>
     *
     * @return хранилище объектов UserEntry.
     */
    public UserEntryStorage getUserEntries() {
        return userEntries;
    }
}
