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
     * <p>Обработчик сообщений, возвращающий ответы на них.</p>
     *
     * <p>В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.</p>
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                        @NotNull LocalMessage message, @NotNull Bot bot);

    /**
     * <p>Вызывать этот метод, чтобы отправилось сообщение,
     * которое должно отправлять при переходе в состояние.</p>
     *
     * <p>TODO: Мне не нравится, что enterMessage() -- это целый метод,
     * возвращающий в большинстве случаев статичный объект.</p>
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    @Nullable
    LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                              @NotNull LocalMessage message, @NotNull Bot bot);
}
