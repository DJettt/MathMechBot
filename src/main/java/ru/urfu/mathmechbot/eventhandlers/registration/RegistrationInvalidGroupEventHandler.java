package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.eventhandlers.GenericInvalidInputEventHandler;
import ru.urfu.mathmechbot.events.InvalidInputEvent;

/**
 * Обрабатывает некорректный ввод номера группы.
 */
public final class RegistrationInvalidGroupEventHandler extends GenericInvalidInputEventHandler {
    @Override
    public void handleEvent(InvalidInputEvent e) {
        super.handleEvent(e);
        e.request().bot().sendMessage(Constants.GROUP, e.request().user().id());
    }
}
