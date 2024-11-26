package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.Utils;
import ru.urfu.mathmechbot.models.User;

/**
 * <p>Отправляет пользователю подтверждение сохранения
 * информации и спрашивает о желании отредактировать что-то ещё.</p>
 */
public final class EditingInfoSaved implements Action<EventContext> {
    public final LocalMessage saved = new LocalMessage("Данные сохранены.");
    public final LocalMessage editAdditional = new LocalMessageBuilder()
            .text("На этом всё?")
            .buttons(new Utils().makeYesNoButtons())
            .build();

    @Override
    public void execute(@NotNull EventContext context) {
        final User user = context.user();
        context.bot().sendMessage(saved, user.id());
        context.bot().sendMessage(editAdditional, user.id());
    }
}
