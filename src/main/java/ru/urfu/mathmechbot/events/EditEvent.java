package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователю надо предложить отредактировать свои данные.</p>
 */
public final class EditEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public EditEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
