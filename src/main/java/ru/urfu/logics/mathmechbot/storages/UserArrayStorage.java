package ru.urfu.logics.mathmechbot.storages;

import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.storages.ArrayStorage;

/**
 * Имплементация UserStorage через ArrayStorage.
 */
public class UserArrayStorage extends ArrayStorage<User, Long> implements UserStorage {
}
