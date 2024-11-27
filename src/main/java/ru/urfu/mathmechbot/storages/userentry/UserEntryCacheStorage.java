package ru.urfu.mathmechbot.storages.userentry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;

/**
 * Содержит в себе информацию о пользователях.
 * Был добавлен в целях упрощения работы бд.
 */
public class UserEntryCacheStorage implements UserEntryStorage {
    private final static int INITIAL_CAPACITY = 5000;
    private final static int DEFAULT_MAX_SIZE = 5000;
    private final int maxSize;
    private final Map<Long, UserEntry> cache = new ConcurrentHashMap<>(INITIAL_CAPACITY);

    /**
     * Конструктор.
     */
    public UserEntryCacheStorage() {
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    /**
     * Конструктор со значением максимального размера.
     * @param maxSize максимальный размер.
     */
    public UserEntryCacheStorage(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .name(name)
                    .build());
        }
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .surname(surname)
                    .build());
        }
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .patronym(patronym)
                    .build());
        }
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .year(year)
                    .build());
        }
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .specialty(specialty)
                    .build());
        }
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id))
                    .men(men)
                    .build());
        }
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        if (cache.containsKey(id)) {
            cache.put(id, new UserEntryBuilder(cache.get(id)).group(group)
                    .build());
        }
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
        if (cache.size() >= maxSize) {
            cache.clear();
        }
        cache.put(member.id(), member);
    }

    @Override
    public void update(UserEntry member) {
        if (cache.containsKey(member.id())) {
            cache.put(member.id(), member);
        }
    }

    @Override
    public void delete(UserEntry member) {
        cache.remove(member.id());
    }
}
