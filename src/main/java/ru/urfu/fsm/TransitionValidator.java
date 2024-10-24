package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Валидатор перехода.</p>
 *
 * @param <E> тип событий.
 * @param <S> тип состояний автомата.
 * @param <C> тип контекста.
 */
final class TransitionValidator<S, E, C> {
    private final Logger logger = LoggerFactory.getLogger(TransitionValidator.class);

    /**
     * <p>Проверяет корректность определённого перехода.</p>
     *
     * @param transition проверяемое состояния.
     * @param fsm        автомат, для которого проверяется корректность перехода.
     */
    void validate(@NotNull Transition<S, E, C> transition,
                  @NotNull StateMachine<S, E, C> fsm)
            throws IllegalArgumentException {

        String name = transition.name();
        S source = transition.source();
        S target = transition.target();

        if (!fsm.getStates().contains(source)) {
            logger.error("Source state '{}' is not registered in FSM states for transition '{}'",
                    source, name);
            throw new IllegalArgumentException();
        }
        if (!fsm.getStates().contains(target)) {
            logger.error("Target state '{}' is not registered in FSM states for transition '{}'",
                    target, name);
            throw new IllegalArgumentException();
        }
    }
}
