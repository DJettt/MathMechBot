package ru.urfu.fsm;

import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация конечного автомата в общем случае.
 *
 * @param <E> тип событий, которые провоцируют переходы.
 * @param <S> тип состояний, между которыми автомат совершает переходы.
 */
public class FiniteStateMachineImpl<
        E extends Event,
        S extends State>
        implements FiniteStateMachine<E, S> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FiniteStateMachineImpl.class);

    private final TransitionValidator<E, S> transitionValidator;
    private final Set<S> states;
    private final Set<Transition<E, S>> transitions;
    private S currentState;

    /**
     * Конструктор.
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
        for (Transition<E, S> transition : transitions) {
            if (!isTransitionSuitable(transition, event)) {
                continue;
            }

            try {
                final EventHandler eventHandler = transition.eventHandler();
                if (eventHandler != null) {
                    eventHandler.handleEvent(event);
                }
                currentState = transition.targetState();
                onTransition(event);
                return;
            } catch (Exception e) {
                LOGGER.error("An exception occurred during handling event {} of transition {}", event, transition, e);
                throw new RuntimeException();
            }
        }

        LOGGER.debug("No transition found for {}. Current state is {}", event, currentState);
    }

    @Override
    public void onTransition(@NotNull E event) {
    }

    @Override
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

    @Override
    public void registerTransition(@NotNull Transition<E, S> transition) {
        transitionValidator.validate(transition, this);
        this.transitions.add(transition);
    }

    /**
     * Проверка того, может ли данный переход обработать данное событие.
     *
     * @param transition проверяемый переход.
     * @param event      полученное событие.
     * @return результат проверки.
     */
    private boolean isTransitionSuitable(Transition<E, S> transition, Event event) {
        return currentState.equals(transition.sourceState())
                && transition.eventType().equals(event.getClass())
                && states.contains(transition.targetState());
    }
}
