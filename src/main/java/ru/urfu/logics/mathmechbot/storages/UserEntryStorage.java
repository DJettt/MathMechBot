package ru.urfu.logics.mathmechbot.storages;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.UserEntryBuilder;
import ru.urfu.storages.Storage;

/**
 * Хранилище объектов модели UserEntry, предоставляющее методы по изменению отдельных полей.
 */
public interface UserEntryStorage extends Storage<UserEntry, Long> {

    /**
     * Меняет поля name, surname.
     * @param id идентификатор записи в хранилище.
     * @param strings ФИО
     */
    default void changeUserEntryNameSurname(@NotNull Long id, @NotNull List<String> strings) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .surname(strings.get(0))
                .name(strings.get(1))
                .build()));
    }

    /**
     * Меняет поле name.
     * @param id идентификатор записи в хранилище.
     * @param name новое значение
     */
    default void changeUserEntryName(@NotNull Long id, @NotNull String name) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .name(name)
                .build()));
    }

    /**
     * Меняет поле surname.
     * @param id идентификатор записи в хранилище.
     * @param surname новое значение
     */
    default void changeUserEntrySurname(@NotNull Long id, @NotNull String surname) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .surname(surname)
                .build()));
    }

    /**
     * Меняет поле patronym.
     * @param id идентификатор записи в хранилище.
     * @param patronym новое значение
     */
    default void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .patronym(patronym)
                .build()));
    }

    /**
     * Меняет поле year.
     *
     * @param id   идентификатор записи в хранилище.
     * @param year новое значение.
     */
    default void changeUserEntryYear(@NotNull Long id, @NotNull Integer year) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .year(year)
                .build()));
    }

    /**
     * Меняет поле specialty.
     *
     * @param id        идентификатор записи в хранилище.
     * @param specialty новое значение.
     */
    default void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .specialty(specialty)
                .build()));
    }

    /**
     * Меняет поле men.
     *
     * @param id  идентификатор записи в хранилище.
     * @param men новое значение.
     */
    default void changeUserEntryMen(@NotNull Long id, @NotNull String men) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .men(men)
                .build()));
    }

    /**
     * Меняет поле group.
     *
     * @param id    идентификатор записи в хранилище.
     * @param group новое значение.
     */
    default void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .group(group)
                .build()));
    }
}
