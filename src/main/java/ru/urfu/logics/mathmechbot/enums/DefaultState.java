package ru.urfu.logics.mathmechbot.enums;

/**
 * Состояние, в котором пользователь находится по умолчанию.
 */
public enum DefaultState implements MathMechBotState {
    DEFAULT;

    @Override
    public Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass() {
        return ru.urfu.logics.mathmechbot.states.DefaultState.class;
    }
}
