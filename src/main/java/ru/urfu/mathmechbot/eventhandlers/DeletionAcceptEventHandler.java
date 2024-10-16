package ru.urfu.mathmechbot.eventhandlers;

import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.AcceptEvent;

/**
 * Обрабатывает согласие пользователя удалить данные.
 */
public final class DeletionAcceptEventHandler implements EventHandler<AcceptEvent> {
    @Override
    public void handleEvent(AcceptEvent e) {
        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .ifPresent(e.request().context().getStorage().getUserEntries()::delete);

        e.request().bot().sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), e.request().user().id());
        e.request().bot().sendMessage(Constants.HELP, e.request().user().id());
    }
}
