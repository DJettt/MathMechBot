package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователь не зарегистрирован.
 */
public final class NotRegisteredEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public NotRegisteredEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
