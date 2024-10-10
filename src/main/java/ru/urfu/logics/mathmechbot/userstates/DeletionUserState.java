package ru.urfu.logics.mathmechbot.userstates;

import ru.urfu.logics.mathmechbot.states.MathMechBotState;
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
    CONFIRMATION(DeletionConfirmationState.INSTANCE);

    private final MathMechBotState stateInstance;

    /**
     * Устанавливает инстанцию состояния.
     *
     * @param stateInstance инстанция состояния.
     */
    DeletionUserState(MathMechBotState stateInstance) {
        this.stateInstance = stateInstance;
    }

    @Override
    public MathMechBotState stateInstance() {
        return stateInstance;
    }
}
