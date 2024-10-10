package ru.urfu.logics.mathmechbot.states;

import ru.urfu.logics.State;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

/**
 * Абстрактное состояние для MathMechBot.
 */
public abstract class MathMechBotState implements State {
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
