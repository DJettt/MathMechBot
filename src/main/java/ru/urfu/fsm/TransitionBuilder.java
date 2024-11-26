package ru.urfu.fsm;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Билдер перехода автомата.</p>
 *
 * @param <S> тип состояния автомата.
 * @param <E> тип события.
 * @param <C> тип контекста.
 */
public class TransitionBuilder<S, E, C> {
    private String name;
    private S source;
    private S target;
    private E event;
    private final List<Action<C>> actions;

    /**
     * <p>Конструктор.</p>
     */
    public TransitionBuilder() {
        this.name = "noname";
        this.actions = new ArrayList<>();
    }

    /**
     * <p>Устанавливает имя перехода.</p>
     *
     * @param name имя перехода.
     * @return себя же.
     */
    public TransitionBuilder<S, E, C> name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * <p>Устанавливает исходное состояние (то, откуда переход).</p>
     *
     * @param source исходное состояние.
     * @return себя же.
     */
    public TransitionBuilder<S, E, C> source(@NotNull S source) {
        this.source = source;
        return this;
    }

    /**
     * <p>Устанавливает целевое состояние (то, куда переход).</p>
     *
     * @param target целевое состояние.
     * @return себя же.
     */
    public TransitionBuilder<S, E, C> target(@NotNull S target) {
        this.target = target;
        return this;
    }

    /**
     * <p>Событие, которое должно провоцировать автомат.</p>
     *
     * @param event события.
     * @return себя же.
     */
    public TransitionBuilder<S, E, C> event(@NotNull E event) {
        this.event = event;
        return this;
    }

    /**
     * <p>Действие, которое запускается при переходе.</p>
     *
     * @param action действие.
     * @return себя же.
     */
    public TransitionBuilder<S, E, C> action(@NotNull Action<C> action) {
        this.actions.add(action);
        return this;
    }

    /**
     * <p>Билдит переход на основе ранее заданных аргументов.</p>
     *
     * @return готовый переход.
     */
    public Transition<S, E, C> build() {
        return new Transition<>(name, source, target, event, actions);
    }
}
