package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователя надо вернуть на шаг назад.</p>
 */
public final class BackEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public BackEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
