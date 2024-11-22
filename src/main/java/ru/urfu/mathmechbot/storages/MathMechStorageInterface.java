package ru.urfu.mathmechbot.storages;

/**
 * <p>Интерфейс структуры, содержащей все хранилища для MathMechBot.</p>
 */
public interface MathMechStorageInterface {
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
