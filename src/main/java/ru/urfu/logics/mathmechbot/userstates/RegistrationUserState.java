package ru.urfu.logics.mathmechbot.userstates;

import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationConfirmationState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationFirstYearSpecialtiesState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationFullNameState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationGroupState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationLaterYearSpecialitiesState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationMenGroupState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationYearState;

/**
 * Состояния пользователя в процессе регистрации.
 * <ol>
 *     <li>Первое состояние внутри этого процесса - <code>NAME</code>.<br/>
 *     Возможны следующие переходы:
 *     <ul>
 *         <li>корректные ФИО: переход на <code>YEAR</code>.</li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>YEAR</code>. Возможны следующие переходы:
 *     <ul>
 *         <li>1: переход в <code>SPECIALTY1</code></li>
 *         <li>2-4: переход на <code>SPECIALTY2</code>.</li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>SPECIALTY1</code>.Возможны следующие переходы:
 *     <ul>
 *         <li>корректное направление для первого курса: переход в <code>GROUP</code></li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>SPECIALTY2</code>. Возможны следующие переходы:
 *     <ul>
 *         <li>корректное направление для не первого курса: переход в <code>GROUP</code></li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>GROUP</code>. Возможны следующие переходы:
 *     <ul>
 *         <li>корректная группа: переход в <code>MEN</code></li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>MEN</code>. Возможны следующие переходы:
 *     <ul>
 *         <li>корректная группа: переход в <code>CONFIRMATION</code></li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 *     <li><code>CONFIRMATION</code>. Возможны следующие переходы:
 *     <ul>
 *         <li>"да": пользователь получает подтверждение от нас, после процесс завершается.</li>
 *         <li>"нет": оповещаем пользователя о возможности вернуться на шаг раньше, состояние не меняем.</li>
 *         <li>иное: оповещаем пользователя о некорректности ответа, состояние не меняем.</li>
 *     </ul>
 *     </li>
 * </ol>
 */
public enum RegistrationUserState implements MathMechBotUserState {
    NAME(RegistrationFullNameState.INSTANCE),
    YEAR(RegistrationYearState.INSTANCE),
    SPECIALTY1(RegistrationFirstYearSpecialtiesState.INSTANCE),
    SPECIALTY2(RegistrationLaterYearSpecialitiesState.INSTANCE),
    GROUP(RegistrationGroupState.INSTANCE),
    MEN(RegistrationMenGroupState.INSTANCE),
    CONFIRMATION(RegistrationConfirmationState.INSTANCE);

    private final MathMechBotState stateInstance;

    /**
     * Устанавливает инстанцию данного состояния.
     *
     * @param stateInstance инстанция состояния.
     */
    RegistrationUserState(MathMechBotState stateInstance) {
        this.stateInstance = stateInstance;
    }

    @Override
    public MathMechBotState stateInstance() {
        return stateInstance;
    }
}
