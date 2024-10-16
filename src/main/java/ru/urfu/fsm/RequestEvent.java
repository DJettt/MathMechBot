package ru.urfu.fsm;

import ru.urfu.localobjects.BotProcessMessageRequest;

public abstract class RequestEvent implements Event {
    private final BotProcessMessageRequest request;

    protected RequestEvent(BotProcessMessageRequest request) {
        this.request = request;
    }

    public BotProcessMessageRequest request() {
        return request;
    }
}
