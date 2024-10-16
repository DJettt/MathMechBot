package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;

/**
 * Структура, содержащая все хранилища для MathMechBot.
 */
public final class MathMechStorage {
    public final UserStorage users;
    public final UserEntryStorage userEntries;

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
}
