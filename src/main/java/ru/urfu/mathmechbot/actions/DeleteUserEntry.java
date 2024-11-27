package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.storages.UserEntryStorage;

/**
 * <p>Удаляет пользовательскую запись.</p>
 */
public final class DeleteUserEntry implements Action<EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public DeleteUserEntry(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        userEntryStorage.get(context.user().id()).ifPresent(userEntryStorage::delete);
    }
}
