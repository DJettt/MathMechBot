package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Интерфейс логического ядра.
 */
public interface LogicCore {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     * @param msg сообщение, которое нужно обработать
     * @param chatId id чата, от кого пришло сообщение
     * @param bot бот, который получил сообщение
     */
    void processMessage(LocalMessage msg, long chatId, Bot bot);
}
