package ru.urfu.mathmechbot.eventhandlers.registration;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.AcceptEvent;

/**
 * Обрабатывает подтверждение регистрации пользователя.
 */
public final class RegistrationAcceptEventHandler implements EventHandler<AcceptEvent> {
    @Override
    public void handleEvent(AcceptEvent e) {
        e.request().bot().sendMessage(new LocalMessageBuilder().text("Сохранил...").build(), e.request().user().id());
        e.request().bot().sendMessage(Constants.HELP, e.request().user().id());
    }
}
