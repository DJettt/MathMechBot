package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.UserState;

/**
 * Интерфейс состояний пользователя для MathMechBot.
 */
public interface MathMechBotUserState extends UserState {
    @Override
    Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass();
}
