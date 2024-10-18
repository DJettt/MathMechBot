package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.storages.Storage;

/**
 * <p>Хранилище объектов модели UserEntry,
 * предоставляющее методы по изменению отдельных полей.</p>
 */
public interface UserEntryStorage extends Storage<UserEntry, Long> {
    /**
     * <p>Меняет поле year.</p>
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
     * <p>Меняет поле specialty.</p>
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
     * <p>Меняет поле men.</p>
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
     * <p>Меняет поле group.</p>
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
