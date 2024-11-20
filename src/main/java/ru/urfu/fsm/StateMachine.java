package ru.urfu.fsm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;


/**
 * <p>Интерфейс конечного автомата.</p>
 *
 * @param <S> тип состояния автомата.
 * @param <E> тип событий автомата.
 * @param <C> тип контекста событий, который будет
 *            использоваться при совершении действий.
 */
public interface StateMachine<S, E, C> {
    /*
     * Вдохновлялся тем, что увидел в документации Spring Boot,
     * но пытался сделать максимально урезанную и простую версию.
     * Реализацию адаптировал из org.jeasy.easy-states.
     */

    /**
     * <p>Запустить событие в FSM.</p>
     *
     * @param event событие.
     * @param context контекст переданного события.
     * @return состояние, которое автомат принял после обработки события.
     */
    @NotNull
    S sendEvent(@NotNull E event, @NotNull C context);

    /**
     * <p>Зарегистрировать переход между состояниями.</p>
     *
     * @param transition переход.
     */
    void registerTransition(@NotNull Transition<S, E, C> transition) throws IllegalArgumentException;

    /**
     * <p>Сеттер текущего состояния. Полезно, когда автомат
     * работает для нескольких пользователей параллельно.</p>
     *
     * @param state состояние, которое автомат должен принять.
     */
    void setState(@NotNull S state);

    /**
     * <p>Геттер текущего состояния.</p>
     *
     * @return текущее состояние.
     */
    @NotNull
    S getState();

    /**
     * <p>Геттер всех состояний.</p>
     *
     * @return все состояния.
     */
    Set<S> getStates();
}
