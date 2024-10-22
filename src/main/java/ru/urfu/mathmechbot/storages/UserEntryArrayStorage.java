package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.storages.ArrayStorage;

/**
 * <p>Имплементация UserEntryStorage через ArrayStorage.</p>
 */
public final class UserEntryArrayStorage
        extends ArrayStorage<UserEntry, Long>
        implements UserEntryStorage {

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .name(name)
                .build()));
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .surname(surname)
                .build()));
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .patronym(patronym)
                .build()));
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .year(year)
                .build()));
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .specialty(specialty)
                .build()));
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .men(men)
                .build()));
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .group(group)
                .build()));
    }
}
