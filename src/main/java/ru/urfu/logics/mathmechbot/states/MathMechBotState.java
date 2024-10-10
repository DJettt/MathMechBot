package ru.urfu.logics.mathmechbot.states;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

/**
 * Интерфейс состояний для MathMechBot.
 */
public interface MathMechBotState {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     *
     * @param context контекст.
     * @param msg     сообщение, которое нужно обработать.
     * @param chatId  id чата, от кого пришло сообщение.
     * @param bot     бот, который получил сообщение.
     */
    void processMessage(MathMechBotCore context, LocalMessage msg, long chatId, Bot bot);

    /**
     * Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при переходе в состояние.
     * TODO: сделать умнее.
     *
     * @param userId id пользователя, с которым ведётся диалог.
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    LocalMessage enterMessage(MathMechBotCore context, long userId);
}
