package ru.urfu.enums;

import ru.urfu.logics.mathmechbot.RegistrationConfirmationState;
import ru.urfu.logics.mathmechbot.RegistrationFirstYearSpecialtiesState;
import ru.urfu.logics.mathmechbot.RegistrationFullNameState;
import ru.urfu.logics.mathmechbot.RegistrationGroupState;
import ru.urfu.logics.mathmechbot.RegistrationLaterYearSpecialitiesState;
import ru.urfu.logics.mathmechbot.RegistrationMenGroupState;
import ru.urfu.logics.mathmechbot.RegistrationYearState;

/**
 * Состояния в процессе регистрации.
 * Пользователь попадает в процесс, отправив команду регистрации.
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
public enum RegistrationState implements State {
    NAME(RegistrationFullNameState.class),
    YEAR(RegistrationYearState.class),
    SPECIALTY1(RegistrationFirstYearSpecialtiesState.class),
    SPECIALTY2(RegistrationLaterYearSpecialitiesState.class),
    GROUP(RegistrationGroupState.class),
    MEN(RegistrationMenGroupState.class),
    CONFIRMATION(RegistrationConfirmationState.class);

    private final Class<? extends ru.urfu.logics.State> stateClass;

    RegistrationState(Class<? extends ru.urfu.logics.State> stateClass) {
        this.stateClass = stateClass;
    }

    public Class<? extends ru.urfu.logics.State> stateClass() {
        return stateClass;
    }
}
