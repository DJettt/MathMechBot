package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.DeclineEvent;

/**
 * Обрабатывает обобщённый отказ.
 */
public class GenericDeclineEventHandler implements EventHandler<DeclineEvent> {
    @Override
    public void handleEvent(DeclineEvent e) {
        e.request().bot().sendMessage(new LocalMessageBuilder().text("Отмена...").build(), e.request().user().id());
        e.request().bot().sendMessage(Constants.HELP, e.request().user().id());
    }
}
