package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Интерфейс логического ядра.
 */
public interface LogicCore {
    /**
     * Обработчик запросов.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     *
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    void processMessage(@NotNull Long chatId, @NotNull LocalMessage message, @NotNull Bot bot);
}
