package ru.urfu.logics.mathmechbot.storages;

public class MathMechStorage {
    public final UserStorage users;
    public final UserEntryStorage userEntries;

    public MathMechStorage(UserStorage userStorage, UserEntryStorage userEntryStorage) {
        this.users = userStorage;
        this.userEntries = userEntryStorage;
    }
}
