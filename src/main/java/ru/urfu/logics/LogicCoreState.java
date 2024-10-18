package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;

/**
 * <p>Состояние логического ядра (см паттерн "Состояние").</p>
 * @param <T> тип контекста.
 */
public interface LogicCoreState<T extends LogicCore> {
    /**
     * <p>Обработчик запросов с сообщением от ядра.</p>
     * @param request запрос от ядра.
     */
    void processMessage(@NotNull ContextProcessMessageRequest<T> request);
}
