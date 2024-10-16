package ru.urfu.mathmechbot.fsm;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.Event;
import ru.urfu.fsm.RequestEvent;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.UserStorage;


/**
 * Реализация конечного автомата для смены состояний пользователя на основе полученных событий.
 */
public class FiniteUserStateMachineImpl implements FiniteUserStateMachine {
    private static final Logger LOGGER = LoggerFactory.getLogger(FiniteUserStateMachineImpl.class);

    private final UserStorage users;
    private final Set<MathMechBotUserState> states;
    private final Set<Transition> transitions;
    private MathMechBotUserState currentState;

    /**
     * Конструктор.
     *
     * @param states       набор состояний.
     * @param initialState изначальное состояние.
     * @param users        хранилище пользователей, чтобы обновлять в нём состояние пользователей.
     */
    public FiniteUserStateMachineImpl(
            Set<MathMechBotUserState> states,
            MathMechBotUserState initialState,
            UserStorage users) {
        currentState = initialState;
        this.states = states;
        this.users = users;
        transitions = new HashSet<>();
    }

    @Override
    public final synchronized MathMechBotUserState dispatch(@NotNull final RequestEvent event) {
        for (Transition transition : transitions) {
            if (!isTransitionSuitable(transition, event)) {
                continue;
            }

            try {
                if (transition.eventHandler() != null) {
                    transition.eventHandler().handleEvent(event);
                }

                final Optional<User> userOptional = users.get(event.request().id());
                if (userOptional.isEmpty()) {
                    LOGGER.warn("No user with given id: {}", event.request().id());
                    return currentState;
                }

                currentState = transition.targetState();

                final User user = userOptional.get();
                users.changeUserState(user.id(), currentState);
                break;
            } catch (Exception e) {
                LOGGER.error("An exception occurred during handling event {} of transition {}", event, transition, e);
                throw new RuntimeException();
            }
        }

        return currentState;
    }

    /**
     * Зарегистрировать переход между состояниями.
     *
     * @param transition переход.
     */
    public void registerTransition(@NotNull Transition transition) {
        transitions.add(transition);
    }

    /**
     * Проверка того, может ли данный переход обработать данное событие.
     *
     * @param transition проверяемый переход.
     * @param event      полученное событие.
     * @return результат проверки.
     */
    private boolean isTransitionSuitable(Transition transition, Event event) {
        return currentState.equals(transition.sourceState())
                && transition.eventType().equals(event.getClass())
                && states.contains(transition.targetState());
    }
}
