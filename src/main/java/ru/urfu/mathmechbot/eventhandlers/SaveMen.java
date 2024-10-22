package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Сохраняет номер МЕН-группы из сообщения.</p>
 */
public final class SaveMen implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryMen(e.request().user().id(), text.trim());
    }
}