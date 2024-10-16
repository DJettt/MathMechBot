package ru.urfu.fsm;

/**
 * Событие, которое может произойти в FSM.
 */
public interface Event {
    /**
     * Геттер имени события.
     *
     * @return имя события.
     */
    String name();
}
