package ru.urfu.logics.mathmechbot.userstates;

import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Состояние, в котором пользователь находится по умолчанию. Из него пользователь может попасть в:
 * <ul>
 *     <li>Первое состояние регистрации (команда <code>/register</code>).</li>
 *     <li>Первое состояние удаления введённых данных (команда <code>/delete</code>).</li>
 * </ul>
 */
public enum DefaultUserState implements MathMechBotUserState {
    DEFAULT;

    @Override
    public MathMechBotState stateInstance() {
        return DefaultState.INSTANCE;
    }
}
