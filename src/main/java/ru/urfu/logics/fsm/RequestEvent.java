package ru.urfu.logics.fsm;

import ru.urfu.fsm.Event;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;

/**
 * Событие с запросом от контекста.
 * @param <T> тип логического ядра, запрос от которого будет положен в событие.
 */
public abstract class RequestEvent<T extends LogicCore> implements Event {
    private final ContextProcessMessageRequest<T> request;

    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public RequestEvent(ContextProcessMessageRequest<T> request) {
        this.request = request;
    }

    /**
     * Геттер запроса.
     * @return запрос от контекста.
     */
    public ContextProcessMessageRequest<T> request() {
        return request;
    }
}
