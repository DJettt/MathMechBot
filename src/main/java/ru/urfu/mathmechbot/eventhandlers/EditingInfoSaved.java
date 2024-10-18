package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Отправляет пользователю подтверждение сохранения
 * информации и спрашивает о желании отредактировать что-то ещё.
 */
public final class EditingInfoSaved implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        e.request().bot().sendMessage(Constants.SAVED, e.request().user().id());
        e.request().bot().sendMessage(Constants.EDIT_ADDITIONAL, e.request().user().id());
    }
}
