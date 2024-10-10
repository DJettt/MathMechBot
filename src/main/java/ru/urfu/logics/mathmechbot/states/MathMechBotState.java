package ru.urfu.logics.mathmechbot.states;

import ru.urfu.logics.LogicCoreState;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

/**
 * Абстрактное состояние для MathMechBot.
 */
public abstract class MathMechBotState implements LogicCoreState {
    protected final MathMechBotCore context;

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    protected MathMechBotState(MathMechBotCore context) {
        this.context = context;
    }
}
