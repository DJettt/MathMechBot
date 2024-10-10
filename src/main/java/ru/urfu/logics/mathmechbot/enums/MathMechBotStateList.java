package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.StateList;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Интерфейс списка состояний для MathMechBot.
 */
public interface MathMechBotStateList extends StateList {
    @Override
    Class<? extends MathMechBotState> stateClass();
}
