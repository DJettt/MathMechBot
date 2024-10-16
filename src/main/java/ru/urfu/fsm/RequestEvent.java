package ru.urfu.fsm;

import ru.urfu.localobjects.BotProcessMessageRequest;

/**
 * Событие с запросом от бота.
 */
public abstract class RequestEvent implements Event {
    private final BotProcessMessageRequest request;

    /**
     * Конструктор.
     *
     * @param request запрос от бота.
     */
    protected RequestEvent(BotProcessMessageRequest request) {
        this.request = request;
    }

    /**
     * Геттер запроса.
     * @return запрос от бота.
     */
    public BotProcessMessageRequest request() {
        return request;
    }
}
