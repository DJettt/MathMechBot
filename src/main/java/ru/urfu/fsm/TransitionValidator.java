package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Валидатор перехода.
 *
 * @param <E> тип событий, наследники которого должны провоцировать переход.
 * @param <S> тип состояний автомата.
 */
final class TransitionValidator<E extends Event, S extends State> {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(TransitionValidator.class);

    /**
     * Проверяет корректность определённого перехода.
     *
     * @param transition проверяемое состояния.
     * @param fms        автомат, для которого проверяется корректность перехода.
     */
    void validate(@NotNull Transition<E, S> transition, @NotNull FiniteStateMachine<E, S> fms) {
        String transitionName = transition.name();
        S sourceState = transition.sourceState();
        S targetState = transition.targetState();

        if (!fms.getStates().contains(sourceState)) {
            LOGGER.error("Source state '{}' is not registered in FSM states for transition '{}'",
                    sourceState, transitionName);
            throw new IllegalArgumentException();
        }
        if (!fms.getStates().contains(targetState)) {
            LOGGER.error("Target state '{}' is not registered in FSM states for transition '{}'",
                    targetState, transitionName);
            throw new IllegalArgumentException();
        }
    }
}
