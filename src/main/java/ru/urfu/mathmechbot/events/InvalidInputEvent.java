package ru.urfu.mathmechbot.events;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователь ввёл некорректную информацию.</p>
 */
public final class InvalidInputEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public InvalidInputEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
