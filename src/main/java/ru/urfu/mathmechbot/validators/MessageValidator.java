package ru.urfu.mathmechbot.validators;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;

/**
 * <p>Валидатор сообщений: проверяет, содержит
 * ли сообщение корректные данные.</p>
 */
public interface MessageValidator {
    /**
     * <p>Проверяет корректность содержимого сообщения.</p>
     *
     * <p>Принимает хранилище и пользователя, так как валидность
     * содержимого сообщения может варьироваться от пользователя
     * к пользователю в зависимости от других данных.</p>
     *
     * @param storage хранилище данных.
     * @param user пользователь, от которого пришло сообщение.
     * @param message проверяемое сообщение.
     * @return результат проверки.
     */
    boolean validateMessageContent(@NotNull MathMechStorage storage,
                                   @NotNull User user,
                                   @NotNull LocalMessage message);
}
