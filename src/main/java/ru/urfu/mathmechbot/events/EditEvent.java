package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо предложить отредактировать свои данные.
 */
public class EditEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public EditEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
