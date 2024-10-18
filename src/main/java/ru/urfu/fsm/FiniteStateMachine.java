package ru.urfu.fsm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;


/**
 * Конечный автомат для смены пользовательских состояний.
 *
 * @param <E> тип событий ядра.
 * @param <S> тип пользовательского состояния.
 */
public interface FiniteStateMachine<
        E extends Event,
        S extends State> {

    /**
     * Запустить событие в FSM.
     * @param event событие с запросом.
     */
    void dispatch(@NotNull E event);

    /**
     * Сеттер текущего состояния.
     * @param state состояние, которое автомат должен принять.
     */
    void setCurrentState(@NotNull S state);

    /**
     * Геттер всех состояний.
     * @return все состояния.
     */
    Set<S> getStates();
}
