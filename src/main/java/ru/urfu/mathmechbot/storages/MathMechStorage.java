package ru.urfu.mathmechbot.storages;

/**
 * <p>Структура, содержащая все хранилища для MathMechBot.</p>
 */
public final class MathMechStorage implements MathMechStorageInterface {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * <p>Конструктор.</p>
     */
    public MathMechStorage() {
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
