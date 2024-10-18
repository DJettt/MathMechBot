package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователя надо зарегистрировать.
 */
public class RegisterEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public RegisterEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
