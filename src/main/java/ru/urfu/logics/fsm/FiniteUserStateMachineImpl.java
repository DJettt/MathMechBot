package ru.urfu.logics.fsm;

import java.util.Set;
import ru.urfu.fsm.FiniteStateMachineImpl;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.LogicCoreState;

/**
 * Реализация конечного автомата для работы с UserState.
 * Рекомендую использовать при написании ботов.
 *
 * @param <C> тип логического ядра-контекста.
 * @param <U> тип состояния пользователя.
 */
public abstract class FiniteUserStateMachineImpl<
        C extends LogicCore,
        U extends UserState<? extends LogicCoreState<C>>>

        extends FiniteStateMachineImpl<RequestEvent<C>, U>
        implements FiniteUserStateMachine<C, RequestEvent<C>, U> {

    /**
     * Конструктор.
     * @param states       набор состояний.
     * @param initialState изначальное состояние.
     */
    public FiniteUserStateMachineImpl(Set<U> states, U initialState) {
        super(states, initialState);
    }
}
