package ru.urfu.fsm;

/**
 * <p>Обработчик события.</p>
 *
 * @param <E> тип обрабатываемого события.
 */
public interface EventHandler<E> {
    /**
     * <p>Обрабатывает событие.</p>
     *
     * @param e событие.
     */
    void handleEvent(E e);
}
