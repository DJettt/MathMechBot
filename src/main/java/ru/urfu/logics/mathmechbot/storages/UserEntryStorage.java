package ru.urfu.logics.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.storages.Storage;

/**
 * Хранилище объектов модели UserEntry, предоставляющее методы по изменению отдельных полей.
 */
public interface UserEntryStorage extends Storage<UserEntry, Long> {
    /**
     * Меняет поле name.
     * @param id идентификатор записи в хранилище.
     * @param name новое значение
     */
    void changeUserEntryName(@NotNull Long id, @NotNull String name);

    /**
     * Меняет поле surname.
     * @param id идентификатор записи в хранилище.
     * @param surname новое значение
     */
    void changeUserEntrySurname(@NotNull Long id, @NotNull String surname);

    /**
     * Меняет поле patronym.
     * @param id идентификатор записи в хранилище.
     * @param patronym новое значение
     */
    void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym);

    /**
     * Меняет поле year.
     *
     * @param id   идентификатор записи в хранилище.
     * @param year новое значение.
     */
    void changeUserEntryYear(@NotNull Long id, @NotNull Integer year);

    /**
     * Меняет поле specialty.
     *
     * @param id        идентификатор записи в хранилище.
     * @param specialty новое значение.
     */
    void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty);

    /**
     * Меняет поле men.
     *
     * @param id  идентификатор записи в хранилище.
     * @param men новое значение.
     */
    void changeUserEntryMen(@NotNull Long id, @NotNull String men);

    /**
     * Меняет поле group.
     *
     * @param id    идентификатор записи в хранилище.
     * @param group новое значение.
     */
    void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group);
}
