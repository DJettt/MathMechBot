package ru.urfu.mathmechbot.storages;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * Хранилище, которое сохраняет всю информацию и в кэш и в БД, но
 * обращается за получением к кэшу.
 */
public class UserEntryFullStorage implements UserEntryStorage {
    private final UserEntryStorage cache = new UserEntryCacheStorage();
    private final UserEntryStorage postgresStorage = new UserEntryPostgresStorage();

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        postgresStorage.changeUserEntryName(id, name);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntryName(id, name);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        postgresStorage.changeUserEntrySurname(id, surname);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntrySurname(id, surname);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        postgresStorage.changeUserEntryPatronym(id, patronym);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntryPatronym(id, patronym);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        postgresStorage.changeUserEntryYear(id, year);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntryYear(id, year);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        postgresStorage.changeUserEntrySpecialty(id, specialty);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntrySpecialty(id, specialty);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        postgresStorage.changeUserEntryMen(id, men);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntryMen(id, men);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        postgresStorage.changeUserEntryGroup(id, group);
        if (cache.get(id).isPresent()) {
            cache.changeUserEntryGroup(id, group);
        } else {
            postgresStorage.get(id).ifPresent(cache::add);
        }
    }

    @Override
    public Optional<UserEntry> get(Long id) {
        Optional<UserEntry> member = cache.get(id);
        if (member.isEmpty()) {
            member = postgresStorage.get(id);
            member.ifPresent(cache::add);
        }
        return member;
    }

    @Override
    public List<UserEntry> getAll() {
        return postgresStorage.getAll();
    }

    @Override
    public void add(UserEntry member) throws IllegalArgumentException {
        postgresStorage.add(member);
        cache.add(member);
    }

    @Override
    public void update(UserEntry member) {
        postgresStorage.update(member);
        if (cache.get(member.id()).isPresent()) {
            cache.update(member);
        } else {
            cache.add(member);
        }
    }

    @Override
    public void delete(UserEntry member) {
        postgresStorage.delete(member);
        if (cache.get(member.id()).isPresent()) {
            cache.delete(member);
        }
    }
}
