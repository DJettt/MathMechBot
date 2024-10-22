package ru.urfu.mathmechbot.validators;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;

/**
 * <p>Валидатор сообщений: проверяет, содержит
 * ли сообщение корректные данные.</p>
 */
public interface MessageValidator {
    /**
     * <p>Проверяет корректность содержимого сообщения.</p>
     *
     * @param message проверяемое сообщение.
     * @return результат проверки.
     */
    boolean validateMessageContent(@NotNull LocalMessage message);
}
