package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.eventhandlers.GenericInvalidInputEventHandler;
import ru.urfu.mathmechbot.events.InvalidInputEvent;

/**
 * Обрабатывает некорректный ввод год обучения.
 */
public final class RegistrationInvalidYearEventHandler extends GenericInvalidInputEventHandler {
    @Override
    public void handleEvent(InvalidInputEvent e) {
        super.handleEvent(e);
        e.request().bot().sendMessage(Constants.YEAR, e.request().user().id());
    }
}
