package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Посылает пользователю информацию зарегистрированные данные.</p>
 */
public final class SendUserInfo implements Action<EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для чтения.
     */
    public SendUserInfo(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final User user = context.user();
        final UserEntry userEntry = userEntryStorage
                .get(user.id())
                .orElseThrow();

        final LocalMessage message =
                new LocalMessage("Данные о Вас:\n\n" + userEntry.toHumanReadable());
        context.bot().sendMessage(message, user.id());
    }
}
