package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Пользователю надо позволить отредактировать направление подготовки.
 */
public class SpecialtyChosenEvent extends RequestEvent<MMBCore> {
    /**
     * Конструктор.
     *
     * @param request запрос от контекста.
     */
    public SpecialtyChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
