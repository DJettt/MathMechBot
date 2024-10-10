package ru.urfu.logics.mathmechbot.storages;

import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.builders.UserEntryBuilder;
import ru.urfu.storages.Storage;

/**
 * Хранилище объектов модели UserEntry, предоставляющее методы по изменению отдельных полей.
 */
public interface UserEntryStorage extends Storage<UserEntry, Long> {
    /**
     * Меняет поле year.
     *
     * @param id   идентификатор записи в хранилище.
     * @param year новое значение.
     */
    default void changeUserEntryYear(Long id, Integer year) {
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
    default void changeUserEntrySpecialty(Long id, String specialty) {
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
    default void changeUserEntryMen(Long id, String men) {
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
    default void changeUserEntryGroup(Long id, Integer group) {
        get(id).ifPresent(userEntry -> update(new UserEntryBuilder(userEntry)
                .group(group)
                .build()));
    }
}
