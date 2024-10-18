package ru.urfu.fsm;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Билдер перехода автомата.
 *
 * @param <E> тип события, наследники которого будут провоцировать переход.
 * @param <S> тип состояния автомата.
 */
public class TransitionBuilder<
        E extends Event,
        S extends State> {

    private String name;
    private S sourceState;
    private S targetState;
    private Class<? extends E> eventType;
    private final List<EventHandler<E>> eventHandlers;

    /**
     * Конструктор.
     */
    public TransitionBuilder() {
        this.name = "noname";
        this.eventHandlers = new ArrayList<>();
    }

    /**
     * Устанавливает имя перехода.
     * @param name имя перехода.
     * @return себя же.
     */
    public TransitionBuilder<E, S> name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Устанавливает исходное состояние (то, откуда переход).
     * @param sourceState исходное состояние.
     * @return себя же.
     */
    public TransitionBuilder<E, S> sourceState(@NotNull S sourceState) {
        this.sourceState = sourceState;
        return this;
    }

    /**
     * Устанавливает целевое состояние (то, куда переход).
     * @param targetState целевое состояние.
     * @return себя же.
     */
    public TransitionBuilder<E, S> targetState(@NotNull S targetState) {
        this.targetState = targetState;
        return this;
    }

    /**
     * Тип события, которое должно провоцировать автомат.
     * @param eventType тип события.
     * @return себя же.
     */
    public TransitionBuilder<E, S> eventType(@NotNull Class<? extends E> eventType) {
        this.eventType = eventType;
        return this;
    }

    /**
     * Обработчик событий, который будет запускаться после перехода.
     * @param eventHandler обработчик.
     * @return себя же.
     */
    public TransitionBuilder<E, S> eventHandler(@NotNull EventHandler<E> eventHandler) {
        this.eventHandlers.add(eventHandler);
        return this;
    }

    /**
     * Билдит переход на основе ранее заданных аргументов.
     * @return готовое состояние.
     */
    public Transition<E, S> build() {
        return new Transition<>(name, sourceState, targetState, eventType, eventHandlers);
    }
}
