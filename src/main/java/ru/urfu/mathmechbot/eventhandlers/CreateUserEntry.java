package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;

/**
 * <p>Создаёт пустую пользовательскую запись.</p>
 */
public final class CreateUserEntry implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final UserEntry newUserEntry = new UserEntryBuilder(
                e.request().user().id(),
                e.request().user().id()).build();

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .add(newUserEntry);
    }
}
