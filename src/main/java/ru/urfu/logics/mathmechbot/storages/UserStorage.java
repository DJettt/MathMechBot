package ru.urfu.logics.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.builders.UserBuilder;
import ru.urfu.logics.mathmechbot.models.userstates.MathMechBotUserState;
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
    default void changeUserState(@NotNull Long id, @NotNull MathMechBotUserState state) {
        update(new UserBuilder(id, state).build());
    }
}
