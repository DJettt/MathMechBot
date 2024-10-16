package ru.urfu.fsm;

public interface EventHandler<E extends Event> {
    void handleEvent(E event) throws Exception;
}
