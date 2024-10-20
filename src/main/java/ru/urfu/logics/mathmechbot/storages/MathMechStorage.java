package ru.urfu.logics.mathmechbot.storages;

/**
 * Структура, содержащая все хранилища для MathMechBot.
 */
public final class MathMechStorage {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * Конструктор.
     */
    public MathMechStorage() {
        this.users = new UserArrayStorage();
        this.userEntries = new UserEntryArrayStorage();
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
