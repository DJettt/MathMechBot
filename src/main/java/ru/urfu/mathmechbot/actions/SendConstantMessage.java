package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.EventContext;

/**
 * <p>Отправляет определённое на этапе создания объекта сообщение.</p>
 */
public final class SendConstantMessage implements Action<EventContext> {
    private final LocalMessage message;

    /**
     * <p>Конструктор.</p>
     *
     * @param message сообщение, которое нужно отравить при запуске действия.
     */
    public SendConstantMessage(@NotNull LocalMessage message) {
        this.message = message;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        context.bot().sendMessage(this.message, context.user().id());
    }
}
