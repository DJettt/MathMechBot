package ru.urfu.fsm;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Реализация конечного автомата
 * для произвольного типа состояний и событий.</p>
 *
 * @param <E> тип событий, которые провоцируют переходы.
 * @param <S> тип состояний, между которыми автомат совершает переходы.
 * @param <C> тип контекста событий, который будет
 *            использоваться при совершении действий.
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {
    private final Logger logger = LoggerFactory.getLogger(StateMachineImpl.class);

    private final TransitionValidator<S, E, C> transitionValidator;
    private final Set<S> states;
    private final Set<Transition<S, E, C>> transitions;
    private S currentState;

    /**
     * <p>Конструктор.</p>
     *
     * @param states       набор состояний.
     * @param initialState изначальное состояние.
     */
    public StateMachineImpl(@NotNull Set<S> states, @NotNull S initialState) {
        currentState = initialState;
        this.states = states;
        transitions = new HashSet<>();
        transitionValidator = new TransitionValidator<>();
    }

    @NotNull
    @Override
    public synchronized S sendEvent(@NotNull E event, @NotNull C context) {
        for (final Transition<S, E, C> transition : transitions) {
            if (!isTransitionSuitable(transition, event)) {
                continue;
            }

            for (final Action<C> action : transition.actions()) {
                action.execute(context);
            }
            currentState = transition.target();
            return currentState;
        }

        logger.debug("No transition found for {}. Current state is {}", event, currentState);
        return currentState;
    }

    @Override
    public void registerTransition(@NotNull Transition<S, E, C> transition) throws IllegalArgumentException {
        transitionValidator.validate(transition, this);
        this.transitions.add(transition);
    }

    /**
     * <p>Проверка того, может ли данный переход обработать данное событие.</p>
     *
     * @param transition проверяемый переход.
     * @param event      полученное событие.
     * @return результат проверки.
     */
    private boolean isTransitionSuitable(Transition<S, E, C> transition, E event) {
        return currentState.equals(transition.source()) && transition.event().equals(event);
    }

    @Override
    public void setState(@NotNull S state) {
        this.currentState = state;
    }

    @NotNull
    @Override
    public S getState() {
        return currentState;
    }

    @Override
    public Set<S> getStates() {
        return states;
    }
}
