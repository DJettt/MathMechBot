package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;

/**
 * Состояние логического ядра (см паттерн "Состояние").
 * @param <T> тип контекста.
 */
public interface LogicCoreState<T extends LogicCore> {
    /**
     * Обработчик запросов с сообщением от ядра.
     * @param request запрос от ядра.
     */
    void processMessage(@NotNull ContextProcessMessageRequest<T> request);
}
