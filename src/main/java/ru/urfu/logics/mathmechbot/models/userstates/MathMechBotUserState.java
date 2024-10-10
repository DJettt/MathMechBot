package ru.urfu.logics.mathmechbot.models.userstates;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Cостояние пользователя в MathMechBot.
 */
public interface MathMechBotUserState {
    /**
     * Возвращает инстанцию состояния логического ядра MathMechBot.
     *
     * @return инстанцию состояния логического ядра MathMechBot.
     */
    @NotNull
    MathMechBotState stateInstance();
}
