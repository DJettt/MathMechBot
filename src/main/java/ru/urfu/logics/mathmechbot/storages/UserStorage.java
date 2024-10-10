package ru.urfu.logics.mathmechbot.storages;

import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.builders.UserBuilder;
import ru.urfu.logics.mathmechbot.userstates.MathMechBotUserState;
import ru.urfu.storages.Storage;

/**
 * Хранилище объектов модели User, предоставляющее методы по изменению отдельных полей.
 */
public interface UserStorage extends Storage<User, Long> {
    /**
     * Меняет поле текущее состояние пользователя.
     *
     * @param id    идентификатор пользователя в хранилище.
     * @param state новое текущее состояние.
     */
    default void changeUserState(Long id, MathMechBotUserState state) {
        final User user = getById(id);
        deleteById(id);
        add(new UserBuilder(user.id(), state).build());
    }
}
