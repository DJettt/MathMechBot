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
    final UserEntryCashStorage hashMap = new UserEntryCashStorage();
    final UserEntryPostgresStorage postgresStorage = new UserEntryPostgresStorage();

    @Override
    public void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        hashMap.changeUserEntryName(id, name);
        postgresStorage.changeUserEntryName(id, name);
    }

    @Override
    public void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        hashMap.changeUserEntrySurname(id, surname);
        postgresStorage.changeUserEntrySurname(id, surname);
    }

    @Override
    public void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        hashMap.changeUserEntryPatronym(id, patronym);
        postgresStorage.changeUserEntryPatronym(id, patronym);
    }

    @Override
    public void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        hashMap.changeUserEntryYear(id, year);
        postgresStorage.changeUserEntryYear(id, year);
    }

    @Override
    public void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        hashMap.changeUserEntrySpecialty(id, specialty);
        postgresStorage.changeUserEntrySpecialty(id, specialty);
    }

    @Override
    public void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        hashMap.changeUserEntryMen(id, men);
        postgresStorage.changeUserEntryMen(id, men);
    }

    @Override
    public void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        hashMap.changeUserEntryGroup(id, group);
        postgresStorage.changeUserEntryGroup(id, group);
    }

    @Override
    public Optional<UserEntry> get(Long id) {
        Optional<UserEntry> member = hashMap.get(id);
        if (member.equals(Optional.empty())) {
            member = postgresStorage.get(id);
            member.ifPresent(hashMap::add);
        }
        return member;
    }

    @Override
    public List<UserEntry> getAll() {
        return postgresStorage.getAll();
    }

    @Override
    public void add(UserEntry member) throws IllegalArgumentException {
        hashMap.add(member);
        postgresStorage.add(member);
    }

    @Override
    public void update(UserEntry member) {
        hashMap.update(member);
        postgresStorage.update(member);
    }

    @Override
    public void delete(UserEntry member) {
        hashMap.delete(member);
        postgresStorage.delete(member);
    }
}
