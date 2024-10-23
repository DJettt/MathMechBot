package ru.urfu.fsm;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Переход в автомате.</p>
 *
 * @param name         имя перехода.
 * @param source  входное состояние.
 * @param target  целевое состояние.
 * @param event    событие, которое провоцирует данный переход.
 * @param actions обработчик этих событий.
 *
 * @param <E> верхняя граница типа события.
 * @param <S> тип состояний автомата.
 * @param <C> тип контекста.
 */
public record Transition<S, E, C>(
        @NotNull String name,
        @NotNull S source,
        @NotNull S target,
        @NotNull E event,
        @NotNull List<Action<C>> actions
) {
}
