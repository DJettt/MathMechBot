package ru.urfu.storages;

import ru.urfu.models.User;

/**
 * Имплементация UserStorage через ArrayStorage.
 */
public class UserArrayStorage extends ArrayStorage<User, Long> implements UserStorage {
}
