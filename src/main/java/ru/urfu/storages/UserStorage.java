package ru.urfu.storages;

import ru.urfu.enums.Process;
import ru.urfu.enums.ProcessState;
import ru.urfu.models.User;
import ru.urfu.models.builders.UserBuilder;

/**
 * Хранилище объектов модели User, предоставляющее методы по изменению отдельных полей.
 */
public interface UserStorage extends Storage<User, Long> {
    /**
     * Меняет поле текущий процесс пользователя.
     *
     * @param id      идентификатор пользователя в хранилище.
     * @param process новый текущий процесс.
     */
    default void changeUserProcess(Long id, Process process) {
        final User user = getById(id);
        deleteById(id);
        add(new UserBuilder(user).currentProcess(process).build());
    }

    /**
     * Меняет поле текущее состояние пользователя.
     *
     * @param id    идентификатор пользователя в хранилище.
     * @param state новое текущее состояние.
     */
    default void changeUserState(Long id, ProcessState state) {
        final User user = getById(id);
        deleteById(id);
        add(new UserBuilder(user).currentState(state).build());
    }
}
