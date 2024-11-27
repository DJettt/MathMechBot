package ru.urfu.mathmechbot.storages;

import ru.urfu.mathmechbot.storages.user.UserStorage;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Интерфейс структуры, содержащей все хранилища для MathMechBot.</p>
 */
public interface MathMechStorage {
    /**
     * <p>Геттер хранилища объектов User.</p>
     *
     * @return хранилище объектов User.
     */
    UserStorage getUsers();

    /**
     * <p>Геттер хранилища объектов UserEntry.</p>
     *
     * @return хранилище объектов UserEntry.
     */
    UserEntryStorage getUserEntries();
}
