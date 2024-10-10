package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Состояние, в котором пользователь находится по умолчанию.
 */
public enum DefaultStateList implements MathMechBotStateList {
    DEFAULT;

    @Override
    public Class<? extends MathMechBotState> stateClass() {
        return DefaultState.class;
    }
}
