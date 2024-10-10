package ru.urfu.logics;

/**
 * Описывает состояния, которые есть у отдельных процессов.
 */
public interface State {
    /**
     * Возвращает класс состояния.
     *
     * @return класс состояния.
     */
    Class<? extends LogicCoreState> stateClass();
}
