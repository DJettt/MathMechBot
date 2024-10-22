package ru.urfu.mathmechbot.storages;

/**
 * <p>Структура, содержащая все хранилища для MathMechBot.</p>
 */
public final class MathMechStorage {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * <p>Конструктор.</p>
     */
    public MathMechStorage() {
        this.users = new UserArrayStorage();
        this.userEntries = new UserEntryArrayStorage();
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
