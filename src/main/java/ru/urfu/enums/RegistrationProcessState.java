package ru.urfu.enums;

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
public enum RegistrationProcessState implements ProcessState {
    NAME,
    YEAR,
    SPECIALTY1,
    SPECIALTY2,
    GROUP,
    MEN,
    CONFIRMATION
}
