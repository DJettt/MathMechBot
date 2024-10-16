package ru.urfu.fsm;

/**
 * Обработчик события.
 *
 * @param <E> тип обрабатываемого события.
 */
public interface EventHandler<E extends Event> {
    /**
     * Обработывает событие.
     *
     * @param event событие.
     */
    void handleEvent(E event);
}
