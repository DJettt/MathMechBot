package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.storages.Storage;

/**
 * <p>Хранилище объектов модели UserEntry,
 * предоставляющее методы по изменению отдельных полей.</p>
 */
public interface UserEntryStorage extends Storage<UserEntry, Long> {
    /**
     * <p>Меняет поле name.</p>
     *
     * @param id   идентификатор записи в хранилище.
     * @param name новое значение
     */
    void changeUserEntryName(@NotNull Long id, @NotNull String name);

    /**
     * <p>Меняет поле surname.</p>
     *
     * @param id      идентификатор записи в хранилище.
     * @param surname новое значение
     */
    void changeUserEntrySurname(@NotNull Long id, @NotNull String surname);

    /**
     * <p>Меняет поле patronym.</p>
     *
     * @param id       идентификатор записи в хранилище.
     * @param patronym новое значение
     */
    void changeUserEntryPatronym(@NotNull Long id, @Nullable String patronym);

    /**
     * <p>Меняет поле year.</p>
     *
     * @param id   идентификатор записи в хранилище.
     * @param year новое значение.
     */
    void changeUserEntryYear(@NotNull Long id, @NotNull Integer year);

    /**
     * <p>Меняет поле specialty.</p>
     *
     * @param id        идентификатор записи в хранилище.
     * @param specialty новое значение.
     */
    void changeUserEntrySpecialty(@NotNull Long id, @NotNull String specialty);

    /**
     * <p>Меняет поле men.</p>
     *
     * @param id  идентификатор записи в хранилище.
     * @param men новое значение.
     */
    void changeUserEntryMen(@NotNull Long id, @NotNull String men);

    /**
     * <p>Меняет поле group.</p>
     *
     * @param id    идентификатор записи в хранилище.
     * @param group новое значение.
     */
    void changeUserEntryGroup(@NotNull Long id, @NotNull Integer group);
}
