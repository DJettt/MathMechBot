package ru.urfu.logics;

import ru.urfu.fsm.Event;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;

/**
 * <p>Событие с запросом от ядра-контекста.</p>
 *
 * @param <T> тип логического ядра, запрос от которого будет положен в событие.
 */
public abstract class RequestEvent<T extends LogicCore> implements Event {
    private final ContextProcessMessageRequest<T> request;

    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public RequestEvent(ContextProcessMessageRequest<T> request) {
        this.request = request;
    }

    /**
     * <p>Геттер запроса.</p>
     *
     * @return запрос от контекста.
     */
    public ContextProcessMessageRequest<T> request() {
        return request;
    }
}
