package ru.urfu.mathmechbot.fsm;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.RequestEvent;
import ru.urfu.mathmechbot.models.MathMechBotUserState;


/**
 * Конечный автомат для смены пользовательских состояний.
 */
public interface FiniteUserStateMachine {
    /**
     * Обработать событие.
     *
     * @param event событие с запросом.
     * @return состояние, которое принял пользователь после обработки события.
     */
    MathMechBotUserState dispatch(@NotNull RequestEvent event);
}
