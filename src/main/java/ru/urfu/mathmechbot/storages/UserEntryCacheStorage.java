package ru.urfu.mathmechbot.storages;

import java.util.List;
import java.util.Map;
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
public class UserEntryCacheStorage implements UserEntryStorage {
    final static int INITIAL_CAPACITY = 5000;
    final static float LOAD_FACTOR = 0.8f;
    private final Map<Long, UserEntry> cache = new ConcurrentHashMap<>(INITIAL_CAPACITY, LOAD_FACTOR);

    /**
     * Конструктор.
     */
    public UserEntryCacheStorage() {

    }

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .name(name)
                .build());
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .surname(surname)
                .build());
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .patronym(patronym)
                .build());
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .year(year)
                .build());
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .specialty(specialty)
                .build());
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .men(men)
                .build());
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        cache.put(id, new UserEntryBuilder(cache.get(id))
                .group(group)
                .build());
    }

    @Override
    public Optional<UserEntry> get(Long id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<UserEntry> getAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public void add(UserEntry member) throws IllegalArgumentException {
        cache.put(member.id(), member);
    }

    @Override
    public void update(UserEntry member) {
        cache.put(member.id(), member);
    }

    @Override
    public void delete(UserEntry member) {
        cache.remove(member.id());
    }
}
