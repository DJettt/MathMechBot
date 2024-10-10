package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.UserState;

/**
 * Cостояние пользователя в MathMechBot.
 */
public interface MathMechBotUserState extends UserState {
    @Override
    Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> userStateClass();
}
