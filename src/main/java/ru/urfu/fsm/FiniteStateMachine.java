package ru.urfu.fsm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;


/**
 * <p>Интерфейс конечного автомата.</p>
 *
 * @param <E> тип событий ядра.
 * @param <S> тип пользовательского состояния.
 */
public interface FiniteStateMachine<E, S> {
    /**
     * <p>Запустить событие в FSM.</p>
     *
     * @param event событие с запросом.
     * @return состояние, которое автомат принял после обработки события.
     */
    S sendEvent(@NotNull E event);

    /**
     * <p>Геттер текущего состояния.</p>
     *
     * @return текущее состояние автомата.
     */
    @NotNull
    S getState();

    /**
     * <p>Сеттер текущего состояния. Полезно, когда автомат
     * работает для нескольких пользователей параллельно.</p>
     *
     * @param state состояние, которое автомат должен принять.
     */
    void setState(@NotNull S state);

    /**
     * <p>Геттер всех состояний.</p>
     *
     * @return все состояния.
     */
    Set<S> getStates();
}
