package ru.urfu.enums;

/**
 * Описывает состояния, которые есть у отдельных процессов.
 */
public interface State {
    Class<? extends ru.urfu.logics.State> stateClass();
}
