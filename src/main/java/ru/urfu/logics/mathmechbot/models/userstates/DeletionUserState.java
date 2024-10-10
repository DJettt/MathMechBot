package ru.urfu.logics.mathmechbot.models.userstates;

import org.jetbrains.annotations.NotNull;
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
    CONFIRMATION;

    @NotNull
    @Override
    public MathMechBotState stateInstance() {
        return DeletionConfirmationState.INSTANCE;
    }
}
