package ru.urfu.mathmechbot.storages.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;

/**
 * Содержит в себе информацию о текущих состояниях пользователей.
 * Был добавлен в целях упрощения работы бд.
 */
public class UserCacheStorage implements UserStorage {
    private final static int INITIAL_CAPACITY = 5000;
    private final static int DEFAULT_MAX_SIZE = 5000;
    private final int maxSize;
    private final Map<Long, User> cache = new ConcurrentHashMap<>(INITIAL_CAPACITY);

    /**
     * Пустой конструктор.
     */
    public UserCacheStorage() {
        this.maxSize = DEFAULT_MAX_SIZE;
    }

    /**
     * Конструктор с установленным максимальным размером.
     * @param size максимальный размер кэша.
     */
    public UserCacheStorage(int size) {
        this.maxSize = size;
    }

    @Override
    public void changeUserState(@NotNull Long id, @NotNull UserState state) {
        if (cache.containsKey(id)) {
            cache.put(id, new User(id, state));
        }
    }

    @Override
    public Optional<User> get(Long id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(cache.values());
    }

    @Override
    public void add(User member) throws IllegalArgumentException {
        if (cache.size() >= maxSize) {
            cache.clear();
        }
        cache.put(member.id(), member);
    }

    @Override
    public void update(User member) {
        if (cache.containsKey(member.id())) {
            cache.put(member.id(), member);
        }
    }

    @Override
    public void delete(User member) {
        cache.remove(member.id());
    }
}
