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
     */
    void dispatch(@NotNull E event);

    /**
     * <p>Сеттер текущего состояния. Полезно, когда автомат
     * работает для нескольких пользователей параллельно.</p>
     *
     * @param state состояние, которое автомат должен принять.
     */
    void setCurrentState(@NotNull S state);

    /**
     * <p>Геттер всех состояний.</p>
     *
     * @return все состояния.
     */
    Set<S> getStates();
}
