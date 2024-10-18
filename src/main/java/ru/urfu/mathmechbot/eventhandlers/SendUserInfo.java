package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * Посылает пользователю информацию зарегистрированные данные.
 */
public final class SendUserInfo implements EventHandler<RequestEvent<MMBCore>> {
    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final User user = e.request().user();
        final UserEntry userEntry = e
                .request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(user.id())
                .orElseThrow();

        final LocalMessage message = new LocalMessageBuilder()
                .text("Данные о Вас:\n\n" + userEntry.toHumanReadable())
                .build();
        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
