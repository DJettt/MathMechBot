package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Действие, которое нужно совершить при переходе между состояниями.</p>
 *
 * @param <C> тип контекста действия.
 */
public interface Action<C> {
    /**
     * <p>Выполняет действие.</p>
     *
     * @param context контекст.
     */
    void execute(@NotNull C context);
}
