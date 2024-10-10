package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Состояние (см. паттерн "State").
 */
public interface State {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     *
     * @param msg    сообщение, которое нужно обработать
     * @param chatId id чата, от кого пришло сообщение
     * @param bot    бот, который получил сообщение
     */
    void processMessage(LocalMessage msg, long chatId, Bot bot);

    /**
     * Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при входе в состояние.
     * TODO: сделать умнее.
     *
     * @param msg    принятое сообщение.
     * @param chatId идентификатор чата.
     * @param bot    бот, который получил сообщение.
     */
    void onEnter(LocalMessage msg, long chatId, Bot bot);
}
