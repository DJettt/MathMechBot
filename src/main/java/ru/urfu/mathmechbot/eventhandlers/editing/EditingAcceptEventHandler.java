package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.AcceptEvent;

/**
 * Обрабатывает согласие пользователя на предложение отредактировать что-то ещё.
 */
public final class EditingAcceptEventHandler implements EventHandler<AcceptEvent> {
    @Override
    public void handleEvent(AcceptEvent e) {
        e.request().bot().sendMessage(new LocalMessageBuilder()
                .text("Изменения успешно сохранены.").build(), e.request().user().id());
        e.request().bot().sendMessage(Constants.HELP, e.request().user().id());
    }
}
