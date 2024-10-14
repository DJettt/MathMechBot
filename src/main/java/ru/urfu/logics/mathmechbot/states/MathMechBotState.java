package ru.urfu.logics.mathmechbot.states;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

/**
 * Интерфейс состояний для MathMechBot.
 */
public interface MathMechBotState {
    /**
     * <p>Обработчик сообщений, возвращающий ответы на них.</p>
     *
     * <p>В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.</p>
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    void processMessage(@NotNull MathMechBotCore context, @NotNull Request request);

    /**
     * <p>Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при переходе в состояние.</p>
     * TODO: сделать умнее.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    @Nullable
    LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request);
}
