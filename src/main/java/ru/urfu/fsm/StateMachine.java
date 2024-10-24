package ru.urfu.fsm;

import java.util.Set;
import org.jetbrains.annotations.NotNull;


/**
 * <p>Интерфейс конечного автомата.</p>
 *
 * @param <E> тип событий ядра.
 * @param <S> тип пользовательского состояния.
 * @param <C> тип контекста событий, который будет
 *            использоваться при совершении действий.
 */
public interface StateMachine<S, E, C> {
    /**
     * <p>Запустить событие в FSM.</p>
     *
     * @param event событие с запросом.
     * @param context контекст переданного события.
     * @return состояние, которое автомат принял после обработки события.
     */
    S sendEvent(@NotNull E event, @NotNull C context);

    /**
     * <p>Зарегистрировать переход между состояниями.</p>
     *
     * @param transition переход.
     */
    void registerTransition(@NotNull Transition<S, E, C> transition);

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
