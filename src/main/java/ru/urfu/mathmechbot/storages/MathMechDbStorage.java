package ru.urfu.mathmechbot.storages;

import ru.urfu.mathmechbot.storages.user.UserFullStorage;
import ru.urfu.mathmechbot.storages.user.UserStorage;
import ru.urfu.mathmechbot.storages.userentry.UserEntryFullStorage;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

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
        this.users = new UserFullStorage();
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
