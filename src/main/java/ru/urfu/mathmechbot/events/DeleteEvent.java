package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо предложить удалить данные.
 */
public class DeleteEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public DeleteEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
