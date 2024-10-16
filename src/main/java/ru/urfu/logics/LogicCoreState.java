package ru.urfu.logics;

/**
 * Состояние логического ядра (см. паттерн "Состояние").
 *
 * @param <T> класс контекста.
 */
public interface LogicCoreState<T extends LogicCore> extends LogicCore {
    /**
     * Устанавливает контекст состояния.
     *
     * @param context контекст.
     */
    void setContext(T context);
}
