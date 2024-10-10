package ru.urfu.logics;

/**
 * Описывает состояния, которые есть у отдельных процессов.
 */
public interface StateList {
    /**
     * Возвращает класс состояния.
     *
     * @return класс состояния.
     */
    Class<? extends State> stateClass();
}
