package ru.urfu.mathmechbot.storages;

/**
 * <p>Структура, содержащая все хранилища для MathMechBot.</p>
 */
public final class MathMechDbStorage implements MathMechStorage {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * <p>Конструктор.</p>
     */
    public MathMechDbStorage() {
        this.users = new UserPostgresStorage();
        this.userEntries = new UserEntryFullStorage();
    }

    @Override
    public UserStorage getUsers() {
        return users;
    }

    @Override
    public UserEntryStorage getUserEntries() {
        return userEntries;
    }
}
