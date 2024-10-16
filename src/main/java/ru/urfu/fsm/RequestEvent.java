package ru.urfu.fsm;

import ru.urfu.localobjects.Request;

public abstract class RequestEvent implements Event {
    private final Request request;

    protected RequestEvent(Request request) {
        this.request = request;
    }

    public Request request() {
        return request;
    }
}
