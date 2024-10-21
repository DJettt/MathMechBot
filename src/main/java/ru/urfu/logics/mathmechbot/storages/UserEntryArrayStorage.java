package ru.urfu.logics.mathmechbot.storages;

import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.storages.ArrayStorage;

/**
 * Имплементация UserEntryStorage через ArrayStorage.
 */
public final class UserEntryArrayStorage extends ArrayStorage<UserEntry, Long> implements UserEntryStorage {
}
