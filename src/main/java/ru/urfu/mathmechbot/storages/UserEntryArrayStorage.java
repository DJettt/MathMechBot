package ru.urfu.mathmechbot.storages;

import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.storages.ArrayStorage;

/**
 * <p>Имплементация UserEntryStorage через ArrayStorage.</p>
 */
public final class UserEntryArrayStorage
        extends ArrayStorage<UserEntry, Long>
        implements UserEntryStorage {
}
