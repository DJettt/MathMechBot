package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.mathmechbot.eventhandlers.GenericDeclineEventHandler;
import ru.urfu.mathmechbot.events.DeclineEvent;

/**
 * Обрабатывает отказ пользователя от регистрации.
 */
public final class RegistrationDeclineEventHandler extends GenericDeclineEventHandler {
    @Override
    public void handleEvent(DeclineEvent e) {
        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .ifPresent(e.request().context().getStorage().getUserEntries()::delete);

        super.handleEvent(e);
    }
}
