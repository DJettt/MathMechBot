package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.mathmechbot.MathMechBotState;

/**
 * Состояние, в котором пользователь находится по умолчанию.
 */
public enum DefaultStateList implements MathMechBotStateList {
    DEFAULT;

    @Override
    public Class<? extends MathMechBotState> stateClass() {
        return ru.urfu.logics.mathmechbot.DefaultState.class;
    }
}
