package ru.urfu.logics.mathmechbot.storages;

/**
 * Структура, содержащая все хранилища для MathMechBot.
 */
public class MathMechStorage {
    public final UserStorage users;
    public final UserEntryStorage userEntries;

    /**
     * Конструктор.
     *
     * @param userStorage      хранилище User.
     * @param userEntryStorage хранилище UserEntry.
     */
    public MathMechStorage(UserStorage userStorage, UserEntryStorage userEntryStorage) {
        this.users = userStorage;
        this.userEntries = userEntryStorage;
    }
}
