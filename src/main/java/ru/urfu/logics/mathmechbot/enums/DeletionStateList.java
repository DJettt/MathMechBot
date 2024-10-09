package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.logics.mathmechbot.DeletionConfirmationState;
import ru.urfu.logics.mathmechbot.MathMechBotState;

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
public enum DeletionStateList implements MathMechBotStateList {
    CONFIRMATION(DeletionConfirmationState.class);

    private final Class<? extends MathMechBotState> stateClass;

    /**
     * Устанавливает класс для данного состояния.
     *
     * @param stateClass класс состояния.
     */
    DeletionStateList(Class<? extends MathMechBotState> stateClass) {
        this.stateClass = stateClass;
    }

    @Override
    public Class<? extends MathMechBotState> stateClass() {
        return stateClass;
    }
}
