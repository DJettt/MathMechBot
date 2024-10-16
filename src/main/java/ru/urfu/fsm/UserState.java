package ru.urfu.fsm;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.LogicCoreState;

/**
 * Состояние пользователя (на каком этапе диалога он находится).
 * @param <C> тип логического ядра (контекста).
 * @param <T> тип состояний контекста.
 */
public interface UserState<C extends LogicCore, T extends LogicCoreState<C>> {

    /**
     * Геттер объекта, реализующего логику для данного состояния пользователя.
     * @return объект, реализующий логику состояния.
     */
    @NotNull
    T logicCoreState();
}
