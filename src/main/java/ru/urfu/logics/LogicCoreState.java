package ru.urfu.logics;

import ru.urfu.localobjects.LocalMessage;

/**
 * Состояние (см. паттерн "State").
 */
public interface LogicCoreState extends LogicCore {
    /**
     * Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при переходе в состояние.
     * TODO: сделать умнее.
     *
     * @param userId id пользователя, с которым ведётся диалог.
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    LocalMessage enterMessage(long userId);
}
