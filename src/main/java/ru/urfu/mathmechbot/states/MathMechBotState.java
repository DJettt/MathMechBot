package ru.urfu.mathmechbot.states;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCoreState;
import ru.urfu.mathmechbot.MathMechBotCore;

/**
 * Интерфейс состояний для MathMechBot.
 */
public abstract class MathMechBotState implements LogicCoreState<MathMechBotCore> {
    private MathMechBotCore context;

    @Override
    public void setContext(MathMechBotCore context) {
        this.context = context;
    }

    /**
     * Геттер поля context.
     *
     * @return контекст состояния.
     */
    protected MathMechBotCore context() {
        return context;
    }

    /**
     * <p>Вызывать этот метод, чтобы отправилось сообщение, которое должно отправлять при переходе в состояние.</p>
     * TODO: сделать умнее.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     * @return сообщение, которое нужно отправить пользователю при переходе в это состояние.
     */
    @Nullable
    public abstract LocalMessage enterMessage(
            @NotNull MathMechBotCore context,
            @NotNull BotProcessMessageRequest request);
}
