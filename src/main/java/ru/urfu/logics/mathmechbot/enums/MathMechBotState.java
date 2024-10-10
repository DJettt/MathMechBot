package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.State;

/**
 * Интерфейс списка состояний для MathMechBot.
 */
public interface MathMechBotState extends State {
    @Override
    Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass();
}
