package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо позволить отредактировать МЕН-группу.
 */
public class MenChosenEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public MenChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
