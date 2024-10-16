package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо позволить отредактировать группу.
 */
public class GroupChosenEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public GroupChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
