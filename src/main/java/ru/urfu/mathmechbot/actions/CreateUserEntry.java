package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Создаёт пустую пользовательскую запись.</p>
 */
public final class CreateUserEntry implements Action<EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public CreateUserEntry(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final UserEntry newUserEntry = new UserEntryBuilder(
                context.user().id(),
                context.user().id()).build();
        userEntryStorage.add(newUserEntry);
    }
}
