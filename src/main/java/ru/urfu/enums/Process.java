package ru.urfu.enums;

/**
 * Перечисление всех процессов.
 * Процесс - действие в несколько шагов, связанное с пользователем.
 */
public interface Process {
    Class<? extends State> stateClass();
}
