package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователь уже зарегистрирован.
 */
public class AlreadyRegisteredEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public AlreadyRegisteredEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
