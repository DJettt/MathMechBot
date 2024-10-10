package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Состояние (см. паттерн "State").
 */
public interface LogicCoreState extends LogicCore {
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
