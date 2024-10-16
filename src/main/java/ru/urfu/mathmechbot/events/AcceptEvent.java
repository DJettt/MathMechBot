package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователь согласился.
 */
public class AcceptEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public AcceptEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
