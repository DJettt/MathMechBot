package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.BotProcessMessageRequest;

/**
 * Интерфейс логического ядра.
 */
public interface LogicCore {
    /**
     * Обработчик запросов.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     *
     * @param request запрос.
     */
    void processMessage(@NotNull BotProcessMessageRequest request);
}
