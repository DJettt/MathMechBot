package ru.urfu.logics.mathmechbot;

import ru.urfu.logics.State;

/**
 * Абстрактное состояние для MathMechBot.
 */
public abstract class MathMechBotState implements State {
    final MathMechBotCore context;

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    protected MathMechBotState(MathMechBotCore context) {
        this.context = context;
    }
}
