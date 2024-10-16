package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователя надо вернуть на шаг назад.
 */
public class BackEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public BackEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
