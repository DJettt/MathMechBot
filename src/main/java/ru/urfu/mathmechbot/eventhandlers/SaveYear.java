package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Сохраняет год обучения из сообщения.
 */
public final class SaveYear implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        final int year = Integer.parseInt(text);
        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryYear(e.request().user().id(), year);
    }
}
