package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;

/**
 * <p>Интерфейс обработчика сообщений для MathMechBot.</p>
 */
public interface MessageHandler {
    /**
     * <p>Обработчик сообщений. Проверяет содержимое сообщения
     * и определяет, под какое событие оно подходит.</p>
     *
     * @param storage хранилище пользовательских записей.
     * @param user пользователь, для которого обрабатывается сообщение.
     * @param message сообщение с данными.
     * @return событие для FSM.
     */
    Event processMessage(@NotNull MathMechStorage storage,
                         @NotNull User user,
                         @NotNull LocalMessage message);
}
