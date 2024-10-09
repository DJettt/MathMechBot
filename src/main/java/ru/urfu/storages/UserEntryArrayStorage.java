package ru.urfu.storages;

import ru.urfu.models.UserEntry;

/**
 * Имплементация UserEntryStorage через ArrayStorage.
 */
public class UserEntryArrayStorage extends ArrayStorage<UserEntry, Long> implements UserEntryStorage {
}
