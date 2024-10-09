package ru.urfu.enums;

import ru.urfu.logics.mathmechbot.DeletionConfirmationState;

/**
 * Состояния в процессе удаления.
 * Пользователь попадает в процесс, отправив команду удаления.
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
public enum DeletionState implements State {
    CONFIRMATION(DeletionConfirmationState.class);

    private final Class<? extends ru.urfu.logics.State> stateClass;

    DeletionState(Class<? extends ru.urfu.logics.State> stateClass) {
        this.stateClass = stateClass;
    }

    @Override
    public Class<? extends ru.urfu.logics.State> stateClass() {
        return stateClass;
    }
}
