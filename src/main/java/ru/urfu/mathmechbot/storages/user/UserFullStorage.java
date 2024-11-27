package ru.urfu.mathmechbot.storages.user;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;

/**
 * Хранилище, которое сохраняет информацию о текущих состояниях и в кэш и в БД, но
 * обращается за получением к кэшу.
 */
public class UserFullStorage implements UserStorage {
    private final UserCacheStorage cache = new UserCacheStorage();
    private final UserPostgresStorage postgresStorage = new UserPostgresStorage();

    @Override
    public void changeUserState(@NotNull Long id, @NotNull UserState state) {
        postgresStorage.changeUserState(id, state);
        if (cache.get(id).isPresent()) {
            cache.changeUserState(id, state);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public Optional<User> get(Long id) {
        Optional<User> member = cache.get(id);
        if (member.isEmpty()) {
            member = postgresStorage.get(id);
            member.ifPresent(cache::add);
        }
        return member;
    }

    @Override
    public List<User> getAll() {
        return postgresStorage.getAll();
    }

    @Override
    public void add(User member) throws IllegalArgumentException {
        postgresStorage.add(member);
        cache.add(member);
    }

    @Override
    public void update(User member) {
        postgresStorage.update(member);
        if (cache.get(member.id()).isPresent()) {
            cache.update(member);
        } else {
            cache.add(member);
        }
    }

    @Override
    public void delete(User member) {
        postgresStorage.delete(member);
        if (cache.get(member.id()).isPresent()) {
            cache.delete(member);
        }
    }
}
