package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователь уже зарегистрирован.</p>
 */
public final class AlreadyRegisteredEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public AlreadyRegisteredEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
