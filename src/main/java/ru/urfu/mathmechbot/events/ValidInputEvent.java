package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователь ввёл корректные данные.
 */
public class ValidInputEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public ValidInputEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
