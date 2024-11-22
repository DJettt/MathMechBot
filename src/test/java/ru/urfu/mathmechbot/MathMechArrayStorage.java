package ru.urfu.mathmechbot;

import ru.urfu.mathmechbot.storages.MathMechStorageInterface;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryStorage;
import ru.urfu.mathmechbot.storages.UserStorage;

/**
 * Реализация двух хранилищ в массивах для запуска тестов.
 */
public class MathMechArrayStorage implements MathMechStorageInterface {
    private final UserStorage users;
    private final UserEntryStorage userEntries;

    /**
     * <p>Конструктор.</p>
     */
    public MathMechArrayStorage() {
        this.users = new UserArrayStorage();
        this.userEntries = new UserEntryArrayStorage();
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
