package ru.urfu.mathmechbot.storages.user;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.storages.ArrayStorage;

/**
 * <p>Имплементация UserStorage через ArrayStorage.</p>
 */
public final class UserArrayStorage
        extends ArrayStorage<User, Long>
        implements UserStorage {

    @Override
    public void changeUserState(@NotNull Long id, @NotNull UserState state) {
        update(new User(id, state));
    }
}
