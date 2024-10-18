package ru.urfu.mathmechbot.eventhandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Посылает определённое на этапе создания сообщение.
 */
public final class SendPredeterminedMessage implements EventHandler<RequestEvent<MMBCore>> {
    private final LocalMessage message;

    /**
     * Конструктор.
     *
     * @param message сообщение, которое нужно отравить при запуске обработчика.
     */
    public SendPredeterminedMessage(@NotNull LocalMessage message) {
        this.message = message;
    }

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        e.request().bot().sendMessage(this.message, e.request().user().id());
    }
}
