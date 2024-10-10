package ru.urfu.logics.mathmechbot.states;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * @param context логического ядро (контекст для состояния).
     * @param msg     сообщение, которое нужно обработать.
     * @param chatId  id чата, от кого пришло сообщение.
     * @param bot     бот, который получил сообщение.
     */
    void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg, long chatId, @NotNull Bot bot);

    /**
     * Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при переходе в состояние.
     * TODO: сделать умнее.
     *
     * @param context логического ядро (контекст для состояния).
     * @param userId id пользователя, с которым ведётся диалог.
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    @Nullable
    LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId);
}
