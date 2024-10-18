package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.State;

/**
 * Состояние пользователя (на каком этапе диалога он находится).
 * @param <T> тип состояний контекста.
 */
public interface UserState<T extends LogicCoreState<? extends LogicCore>> extends State {

    /**
     * Геттер объекта, реализующего логику для данного состояния пользователя.
     * @return объект, реализующий логику состояния.
     */
    @NotNull
    T logicCoreState();
}
