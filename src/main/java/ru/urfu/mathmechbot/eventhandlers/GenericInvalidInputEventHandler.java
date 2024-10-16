package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.InvalidInputEvent;

/**
 * Обрабатывает обобщённый некорректный ввод.
 */
public class GenericInvalidInputEventHandler implements EventHandler<InvalidInputEvent> {
    @Override
    public void handleEvent(InvalidInputEvent e) {
        e.request().bot().sendMessage(Constants.TRY_AGAIN, e.request().user().id());
    }
}
