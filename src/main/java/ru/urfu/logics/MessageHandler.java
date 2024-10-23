package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;

/**
 * <p>Обработчик сообщений.</p>
 *
 * @param <T> тип контекста.
 * @param <E> тип событий, возникающих в результате обработки сообщений.
 */
public interface MessageHandler<T extends LogicCore, E> {
    /**
     * <p>Обработчик сообщением.</p>
     *
     * @param request запрос от ядра.
     * @return событие.
     */
    E processMessage(@NotNull ContextProcessMessageRequest<T> request);
}
