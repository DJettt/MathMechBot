package ru.urfu.fsm;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Билдер перехода автомата.</p>
 *
 * @param <E> тип события, наследники которого будут провоцировать переход.
 * @param <S> тип состояния автомата.
 */
public class TransitionBuilder<E, S> {
    private String name;
    private S source;
    private S target;
    private Class<? extends E> eventType;
    private final List<EventHandler<E>> eventHandlers;

    /**
     * <p>Конструктор.</p>
     */
    public TransitionBuilder() {
        this.name = "noname";
        this.eventHandlers = new ArrayList<>();
    }

    /**
     * <p>Устанавливает имя перехода.</p>
     *
     * @param name имя перехода.
     * @return себя же.
     */
    public TransitionBuilder<E, S> name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * <p>Устанавливает исходное состояние (то, откуда переход).</p>
     *
     * @param source исходное состояние.
     * @return себя же.
     */
    public TransitionBuilder<E, S> source(@NotNull S source) {
        this.source = source;
        return this;
    }

    /**
     * <p>Устанавливает целевое состояние (то, куда переход).</p>
     *
     * @param target целевое состояние.
     * @return себя же.
     */
    public TransitionBuilder<E, S> target(@NotNull S target) {
        this.target = target;
        return this;
    }

    /**
     * <p>Тип события, которое должно провоцировать автомат.</p>
     *
     * @param eventType тип события.
     * @return себя же.
     */
    public TransitionBuilder<E, S> eventType(@NotNull Class<? extends E> eventType) {
        this.eventType = eventType;
        return this;
    }

    /**
     * <p>Обработчик событий, который будет запускаться после перехода.</p>
     *
     * @param eventHandler обработчик.
     * @return себя же.
     */
    public TransitionBuilder<E, S> eventHandler(@NotNull EventHandler<E> eventHandler) {
        this.eventHandlers.add(eventHandler);
        return this;
    }

    /**
     * <p>Билдит переход на основе ранее заданных аргументов.</p>
     *
     * @return готовый переход.
     */
    public Transition<E, S> build() {
        return new Transition<>(name, source, target, eventType, eventHandlers);
    }
}
