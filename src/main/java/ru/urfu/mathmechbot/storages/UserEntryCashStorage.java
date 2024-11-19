package ru.urfu.mathmechbot.storages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;

/**
 * Содержит в себе всю информацию о пользователях.
 * Был добавлен в целях упрощения работы бд.
 */
public class UserEntryCashStorage implements UserEntryStorage {
    final static int INITIAL_CAPACITY = 5000;
    final static float LOAD_FACTOR = 0.8f;
    private final ConcurrentHashMap<Long, UserEntry> hashMap = new ConcurrentHashMap(INITIAL_CAPACITY, LOAD_FACTOR);

    /**
     * Конструктор.
     */
    public UserEntryCashStorage() {

    }

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .name(name)
                .build());
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .surname(surname)
                .build());
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .patronym(patronym)
                .build());
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .year(year)
                .build());
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .specialty(specialty)
                .build());
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .men(men)
                .build());
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        hashMap.put(id, new UserEntryBuilder(hashMap.get(id))
                .group(group)
                .build());
    }

    @Override
    public Optional<UserEntry> get(Long id) {
        return Optional.ofNullable(hashMap.get(id));
    }

    @Override
    public List<UserEntry> getAll() {
        return new ArrayList<>(hashMap.values());
    }

    @Override
    public void add(UserEntry member) throws IllegalArgumentException {
        hashMap.put(member.id(), member);
    }

    @Override
    public void update(UserEntry member) {
        hashMap.put(member.id(), member);
    }

    @Override
    public void delete(UserEntry member) {
        hashMap.remove(member.id());
    }
}
