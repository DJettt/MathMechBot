package ru.urfu.mathmechbot.events.editing;

import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Пользователю надо позволить отредактировать направление подготовки.</p>
 */
public final class SpecialtyChosenEvent extends RequestEvent<MMBCore> {
    /**
     * <p>Конструктор.</p>
     *
     * @param request запрос от контекста.
     */
    public SpecialtyChosenEvent(ContextProcessMessageRequest<MMBCore> request) {
        super(request);
    }
}
