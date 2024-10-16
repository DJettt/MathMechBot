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
     *
     * @param event событие с запросом.
     */
    void dispatch(@NotNull E event);

    /**
     * Фрагмент кода, который будет выполняться при переходе в другое состояние.
     *
     * @param event событие, которое спровоцировало переход.
     */
    void onTransition(@NotNull E event);

    /**
     * Геттер текущего состояния.
     *
     * @return текущее состояние.
     */
    S getCurrentState();

    /**
     * Сеттер текущего состояния.
     *
     * @param state состояние, которое автомат должен принять.
     */
    void setCurrentState(@NotNull S state);

    /**
     * Геттер всех состояний.
     *
     * @return все состояния.
     */
    Set<S> getStates();

    /**
     * Зарегистрировать переход между состояниями.
     *
     * @param transition переход.
     */
    void registerTransition(@NotNull Transition<E, S> transition);
}
