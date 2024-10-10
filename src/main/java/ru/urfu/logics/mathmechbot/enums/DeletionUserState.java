package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.mathmechbot.states.deletion.DeletionConfirmationState;

/**
 * Состояния пользователя в процессе удаления.
 * <ol>
 *     <li>Первое состояние внутри этого процесса - <code>CONFIRMATION</code>.<br/>
 *     Возможны следующие переходы:
 *     <ul>
 *         <li>"да": пользователь получает подтверждение от нас, после процесс завершается.</li>
 *         <li>"нет": пользователь получает подтверждение от нас, после процесс завершается.</li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 * </ol>
 */
public enum DeletionUserState implements MathMechBotUserState {
    CONFIRMATION(DeletionConfirmationState.class);

    private final Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass;

    /**
     * Устанавливает класс для данного состояния.
     *
     * @param stateClass класс состояния.
     */
    DeletionUserState(Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass) {
        this.stateClass = stateClass;
    }

    @Override
    public Class<? extends ru.urfu.logics.mathmechbot.states.MathMechBotState> stateClass() {
        return stateClass;
    }
}
