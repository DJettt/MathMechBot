package ru.urfu.mathmechbot;

import ru.urfu.fsm.TransitionBuilder;
import ru.urfu.logics.fsm.RequestEvent;

/**
 * Билдер объектов Transition с предустановленными типами для MathMechBot.
 */
public class MMBTransitionBuilder extends TransitionBuilder<RequestEvent<MMBCore>, MMBUserState> {
}
