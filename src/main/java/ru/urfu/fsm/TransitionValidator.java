package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Валидатор перехода.</p>
 *
 * @param <E> тип событий, наследники которого должны провоцировать переход.
 * @param <S> тип состояний автомата.
 */
final class TransitionValidator<E extends Event, S extends State> {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(TransitionValidator.class);

    /**
     * <p>Проверяет корректность определённого перехода.</p>
     *
     * @param transition проверяемое состояния.
     * @param fms        автомат, для которого проверяется корректность перехода.
     */
    void validate(@NotNull Transition<E, S> transition,
                  @NotNull FiniteStateMachine<E, S> fms) {

        String name = transition.name();
        S source = transition.source();
        S target = transition.target();

        if (!fms.getStates().contains(source)) {
            LOGGER.error("Source state '{}' is not registered in FSM states for transition '{}'",
                    source, name);
            throw new IllegalArgumentException();
        }
        if (!fms.getStates().contains(target)) {
            LOGGER.error("Target state '{}' is not registered in FSM states for transition '{}'",
                    target, name);
            throw new IllegalArgumentException();
        }
    }
}
