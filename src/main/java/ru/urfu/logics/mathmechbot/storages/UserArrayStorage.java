package ru.urfu.logics.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.storages.ArrayStorage;

/**
 * Имплементация UserStorage через ArrayStorage.
 */
public final class UserArrayStorage extends ArrayStorage<User, Long> implements UserStorage {
    @Override
    public void changeUserState(@NotNull Long id, @NotNull MathMechBotUserState state) {
        update(new User(id, state));
    }
}
