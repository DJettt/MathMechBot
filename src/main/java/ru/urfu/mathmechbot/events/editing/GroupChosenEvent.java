package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователю надо позволить отредактировать группу.</p>
 */
public final class GroupChosenEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public GroupChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
