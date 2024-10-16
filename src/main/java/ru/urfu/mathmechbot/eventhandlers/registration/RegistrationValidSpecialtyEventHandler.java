package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.fsm.EventHandler;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод специальности.
 */
public final class RegistrationValidSpecialtyEventHandler implements EventHandler<ValidInputEvent> {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntrySpecialty(e.request().user().id(), text);
        e.request().bot().sendMessage(Constants.GROUP, e.request().user().id());
    }
}
