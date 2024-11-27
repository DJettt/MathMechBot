package ru.urfu.mathmechbot;

import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.user.UserArrayStorage;
import ru.urfu.mathmechbot.storages.user.UserStorage;
import ru.urfu.mathmechbot.storages.userentry.UserEntryArrayStorage;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * Реализация двух хранилищ в массивах для запуска тестов.
 */
public class MathMechArrayStorage implements MathMechStorage {
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
