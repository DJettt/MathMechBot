package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.fsm.EventHandler;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод номера группы.
 */
public final class RegistrationValidGroupEventHandler implements EventHandler<ValidInputEvent> {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryGroup(e.request().user().id(), Integer.parseInt(text));

        e.request().bot().sendMessage(Constants.MEN, e.request().user().id());
    }
}
