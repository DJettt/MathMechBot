package ru.urfu.enums;

/**
 * Описывает состояния, которые есть у отдельных процессов.
 */
public interface StateList {
    /**
     * Возвращает класс состояния.
     *
     * @return класс состояния.
     */
    Class<? extends ru.urfu.logics.State> stateClass();
}
