package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Переход в автомате.
 *
 * @param name         имя перехода.
 * @param sourceState  входное состояние.
 * @param targetState  целевое состояние.
 * @param eventType    класс событий, наследники которого провоцируют данный переход.
 * @param eventHandler обработчик этих событий.
 *
 * @param <E> верхняя граница типа события.
 * @param <S> тип состояний автомата.
 */
public record Transition<
        E extends Event,
        S extends State>(
        @NotNull String name,
        @NotNull S sourceState,
        @NotNull S targetState,
        @NotNull Class<? extends E> eventType,
        @Nullable EventHandler eventHandler
) {
}
