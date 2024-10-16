package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.fsm.EventHandler;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод некоторой информации во время редактирования.
 */
public class GenericEditingValidEventHandler implements EventHandler<ValidInputEvent> {
    @Override
    public void handleEvent(ValidInputEvent e) {
        e.request().bot().sendMessage(Constants.SAVED, e.request().user().id());
        e.request().bot().sendMessage(Constants.EDIT_ADDITIONAL, e.request().user().id());
    }
}
