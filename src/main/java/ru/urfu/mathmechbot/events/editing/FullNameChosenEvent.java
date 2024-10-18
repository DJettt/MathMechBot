package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо позволить отредактировать ФИО.
 */
public class FullNameChosenEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public FullNameChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
