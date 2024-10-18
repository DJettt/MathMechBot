package ru.urfu.mathmechbot;

import ru.urfu.fsm.TransitionBuilder;
import ru.urfu.logics.RequestEvent;

/**
 * <p>Билдер объектов Transition с предустановленными типами для MathMechBot.</p>
 */
public final class MMBTransitionBuilder
        extends TransitionBuilder<RequestEvent<MMBCore>, MMBUserState> {
}
