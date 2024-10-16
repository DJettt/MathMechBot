package ru.urfu.fsm;

/**
 * Обработчик события.
 * @param <E> тип обрабатываемого события.
 */
public interface EventHandler<E extends Event> {
    /**
     * Обрабатывает событие.
     * @param e событие.
     */
    void handleEvent(E e);
}
