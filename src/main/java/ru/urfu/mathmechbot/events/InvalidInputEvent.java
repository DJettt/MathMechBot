package ru.urfu.mathmechbot.events;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователь ввёл некорректную информацию.
 */
public class InvalidInputEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     * @param request запрос от контекста.
     */
    public InvalidInputEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
