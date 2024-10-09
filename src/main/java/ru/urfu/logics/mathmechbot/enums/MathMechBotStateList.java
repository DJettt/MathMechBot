package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.enums.StateList;
import ru.urfu.logics.mathmechbot.MathMechBotState;

/**
 * Интерфейс списка состояний для MathMechBot.
 */
public interface MathMechBotStateList extends StateList {
    @Override
    Class<? extends MathMechBotState> stateClass();
}
