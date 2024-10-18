package ru.urfu.fsm;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Переход в автомате.</p>
 *
 * @param name         имя перехода.
 * @param sourceState  входное состояние.
 * @param targetState  целевое состояние.
 * @param eventType    класс событий, наследники которого провоцируют данный переход.
 * @param eventHandlers обработчик этих событий.
 *
 * @param <E> верхняя граница типа события.
 * @param <S> тип состояний автомата.
 */
public record Transition<E extends Event, S extends State>(
        @NotNull String name,
        @NotNull S sourceState,
        @NotNull S targetState,
        @NotNull Class<? extends E> eventType,
        @NotNull List<EventHandler<E>> eventHandlers
) {
}
