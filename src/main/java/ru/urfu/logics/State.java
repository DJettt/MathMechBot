package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

public abstract class State {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     *
     * @param msg    сообщение, которое нужно обработать
     * @param chatId id чата, от кого пришло сообщение
     * @param bot    бот, который получил сообщение
     */
    public abstract void processMessage(LocalMessage msg, long chatId, Bot bot);

    public abstract void onEnter(LocalMessage msg, long chatId, Bot bot);
}
