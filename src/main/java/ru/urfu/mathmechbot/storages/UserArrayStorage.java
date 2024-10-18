package ru.urfu.mathmechbot.storages;

import ru.urfu.mathmechbot.models.User;
import ru.urfu.storages.ArrayStorage;

/**
 * <p>Имплементация UserStorage через ArrayStorage.</p>
 */
public final class UserArrayStorage
        extends ArrayStorage<User, Long>
        implements UserStorage {
}
