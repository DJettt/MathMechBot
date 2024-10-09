package ru.urfu.storages;

import ru.urfu.models.UserEntry;
import ru.urfu.models.builders.UserEntryBuilder;

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
        final UserEntry userEntry = getById(id);
        deleteById(id);
        add(new UserEntryBuilder(userEntry)
                .year(year)
                .build());
    }

    /**
     * Меняет поле specialty.
     *
     * @param id        идентификатор записи в хранилище.
     * @param specialty новое значение.
     */
    default void changeUserEntrySpecialty(Long id, String specialty) {
        final UserEntry userEntry = getById(id);
        deleteById(id);
        add(new UserEntryBuilder(userEntry)
                .specialty(specialty)
                .build());
    }

    /**
     * Меняет поле men.
     *
     * @param id  идентификатор записи в хранилище.
     * @param men новое значение.
     */
    default void changeUserEntryMen(Long id, String men) {
        final UserEntry userEntry = getById(id);
        deleteById(id);
        add(new UserEntryBuilder(userEntry)
                .men(men)
                .build());
    }

    /**
     * Меняет поле group.
     *
     * @param id    идентификатор записи в хранилище.
     * @param group новое значение.
     */
    default void changeUserEntryGroup(Long id, Integer group) {
        final UserEntry userEntry = getById(id);
        deleteById(id);
        add(new UserEntryBuilder(userEntry)
                .group(group)
                .build());
    }
}
