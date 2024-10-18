package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Обрабатывает обобщённый некорректный ввод.
 */
public final class TryAgain implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        e.request().bot().sendMessage(Constants.TRY_AGAIN, e.request().user().id());
    }
}
