package ru.urfu.logics.mathmechbot.storages;

import ru.urfu.enums.State;
import ru.urfu.logics.mathmechbot.enums.MathMechBotProcess;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.builders.UserBuilder;
import ru.urfu.storages.Storage;

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
    default void changeUserProcess(Long id, MathMechBotProcess process) {
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
    default void changeUserState(Long id, State state) {
        final User user = getById(id);
        deleteById(id);
        add(new UserBuilder(user).currentState(state).build());
    }
}
