package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;

/**
 * Структура, содержащая все хранилища для MathMechBot.
 */
public final class MathMechStorage {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * Конструктор.
     *
     * @param userStorage      хранилище User.
     * @param userEntryStorage хранилище UserEntry.
     */
    public MathMechStorage(@NotNull UserStorage userStorage, @NotNull UserEntryStorage userEntryStorage) {
        this.users = userStorage;
        this.userEntries = userEntryStorage;
    }

    /**
     * Геттер хранилища объектов User.
     *
     * @return хранилище объектов User.
     */
    public UserStorage getUsers() {
        return users;
    }

    /**
     * Геттер хранилища объектов UserEntry.
     *
     * @return хранилище объектов UserEntry.
     */
    public UserEntryStorage getUserEntries() {
        return userEntries;
    }
}
