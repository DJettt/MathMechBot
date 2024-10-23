package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.User;

/**
 * <p>Отправляет пользователю подтверждение сохранения
 * информации и спрашивает о желании отредактировать что-то ещё.</p>
 */
public final class EditingInfoSaved implements Action<EventContext> {
    @Override
    public void execute(@NotNull EventContext context) {
        final User user = context.user();
        context.bot().sendMessage(Constants.SAVED, user.id());
        context.bot().sendMessage(Constants.EDIT_ADDITIONAL, user.id());
    }
}
