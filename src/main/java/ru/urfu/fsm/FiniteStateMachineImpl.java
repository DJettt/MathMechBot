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
 */
public class FiniteStateMachineImpl<E, S> implements FiniteStateMachine<E, S> {
    private final Logger logger = LoggerFactory
            .getLogger(FiniteStateMachineImpl.class);

    private final TransitionValidator<E, S> transitionValidator;
    private final Set<S> states;
    private final Set<Transition<E, S>> transitions;
    private S currentState;

    /**
     * <p>Конструктор.</p>
     *
     * @param states       набор состояний.
     * @param initialState изначальное состояние.
     */
    public FiniteStateMachineImpl(Set<S> states, S initialState) {
        currentState = initialState;
        this.states = states;
        transitions = new HashSet<>();
        transitionValidator = new TransitionValidator<>();
    }

    @Override
    public void dispatch(@NotNull E event) {
        for (final Transition<E, S> transition : transitions) {
            if (!isTransitionSuitable(transition, event)) {
                continue;
            }

            for (final EventHandler<E> eventHandler : transition.eventHandlers()) {
                if (eventHandler != null) {
                    eventHandler.handleEvent(event);
                }
            }

            currentState = transition.target();
            onTransition(event);
            return;
        }

        logger.debug("No transition found for {}. Current state is {}",
                event, currentState);
    }

    /**
     * <p>Фрагмент кода, который будет выполняться
     * при переходе в другое состояние.</p>
     *
     * @param event событие, которое спровоцировало переход.
     */
    public void onTransition(@NotNull E event) {
    }

    /**
     * <p>Зарегистрировать переход между состояниями.</p>
     *
     * @param transition переход.
     */
    public void registerTransition(@NotNull Transition<E, S> transition) {
        transitionValidator.validate(transition, this);
        this.transitions.add(transition);
    }

    /**
     * <p>Геттер текущего состояния.</p>
     *
     * @return текущее состояние.
     */
    public S getCurrentState() {
        return currentState;
    }

    @Override
    public void setCurrentState(@NotNull S state) {
        this.currentState = state;
    }

    @Override
    public Set<S> getStates() {
        return states;
    }

    /**
     * <p>Проверка того, может ли данный переход обработать данное событие.</p>
     *
     * @param transition проверяемый переход.
     * @param event      полученное событие.
     * @return результат проверки.
     */
    private boolean isTransitionSuitable(Transition<E, S> transition, E event) {
        return currentState.equals(transition.source())
                && transition.eventType().equals(event.getClass())
                && states.contains(transition.target());
    }
}
