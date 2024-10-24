package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Интерфейс обработчика сообщений для MathMechBot.</p>
 */
public interface MessageHandler {
    /**
     * <p>Обработчик сообщений. Проверяет содержимое сообщения
     * и определяет, под какое событие оно подходит.</p>
     *
     * @param request запрос от ядра.
     * @return событие для FSM.
     */
    Event processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request);
}