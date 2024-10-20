package ru.urfu.logics.mathmechbot.storages;

/**
 * Структура, содержащая все хранилища для MathMechBot.
 */
public final class MathMechStorage {
    public final UserStorage users;
    public final UserEntryStorage userEntries;

    /**
     * Конструктор.
     */
    public MathMechStorage() {
        this.users = new UserArrayStorage();
        this.userEntries = new UserEntryArrayStorage();
    }
}
