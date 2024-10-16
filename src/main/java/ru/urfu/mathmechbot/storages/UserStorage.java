package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.MMBUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserBuilder;
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
    default void changeUserState(@NotNull Long id, @NotNull MMBUserState state) {
        update(new UserBuilder(id, state).build());
    }
}
