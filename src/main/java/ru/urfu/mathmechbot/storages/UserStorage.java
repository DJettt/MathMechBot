package ru.urfu.mathmechbot.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.storages.Storage;

/**
 * <p>Хранилище объектов модели User, предоставляющее
 * методы по изменению отдельных полей.</p>
 */
public interface UserStorage
        extends Storage<User, Long> {

    /**
     * <p>Меняет поле текущее состояние пользователя.</p>
     *
     * @param id    идентификатор пользователя в хранилище.
     * @param state новое текущее состояние.
     */
    void changeUserState(@NotNull Long id, @NotNull UserState state);
}
