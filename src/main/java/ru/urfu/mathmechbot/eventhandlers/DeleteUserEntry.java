package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Удаляет пользовательскую запись.
 */
public final class DeleteUserEntry implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .ifPresent(e.request().context().getStorage().getUserEntries()::delete);
    }
}
