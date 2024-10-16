package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо выдать зарегистрированные данные.
 */
public class InfoEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public InfoEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
