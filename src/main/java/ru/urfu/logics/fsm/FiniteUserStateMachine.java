package ru.urfu.logics.fsm;

import ru.urfu.fsm.FiniteStateMachine;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.LogicCoreState;


/**
 * Конечный автомат для смены пользовательских состояний.
 *
 * @param <C> тип логического ядра.
 * @param <E> тип событий ядра.
 * @param <U> тип пользовательского состояния.
 */
public interface FiniteUserStateMachine<
        C extends LogicCore,
        E extends RequestEvent<C>,
        U extends UserState<? extends LogicCoreState<C>>>
        extends FiniteStateMachine<E, U> {
}
