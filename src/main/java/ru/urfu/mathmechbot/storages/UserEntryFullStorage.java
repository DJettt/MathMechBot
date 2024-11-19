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
    final UserEntryCacheStorage cache = new UserEntryCacheStorage();
    final UserEntryPostgresStorage postgresStorage = new UserEntryPostgresStorage();

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        postgresStorage.changeUserEntryName(id, name);
        cache.changeUserEntryName(id, name);
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        postgresStorage.changeUserEntrySurname(id, surname);
        cache.changeUserEntrySurname(id, surname);
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        postgresStorage.changeUserEntryPatronym(id, patronym);
        cache.changeUserEntryPatronym(id, patronym);
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        postgresStorage.changeUserEntryYear(id, year);
        cache.changeUserEntryYear(id, year);
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        postgresStorage.changeUserEntrySpecialty(id, specialty);
        cache.changeUserEntrySpecialty(id, specialty);
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        postgresStorage.changeUserEntryMen(id, men);
        cache.changeUserEntryMen(id, men);
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        postgresStorage.changeUserEntryGroup(id, group);
        cache.changeUserEntryGroup(id, group);
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
        cache.update(member);
    }

    @Override
    public void delete(UserEntry member) {
        postgresStorage.delete(member);
        cache.delete(member);
    }
}
