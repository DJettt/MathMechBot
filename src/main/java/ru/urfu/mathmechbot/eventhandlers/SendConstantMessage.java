package ru.urfu.mathmechbot.eventhandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Отправляет определённое на этапе создания сообщение.</p>
 */
public final class SendConstantMessage implements EventHandler<RequestEvent<MMBCore>> {
    private final LocalMessage message;

    /**
     * <p>Конструктор.</p>
     *
     * @param message сообщение, которое нужно отравить при запуске обработчика.
     */
    public SendConstantMessage(@NotNull LocalMessage message) {
        this.message = message;
    }

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        e.request().bot().sendMessage(this.message, e.request().user().id());
    }
}
