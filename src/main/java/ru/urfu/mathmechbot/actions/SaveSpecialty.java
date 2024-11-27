package ru.urfu.mathmechbot.actions;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Сохраняет направление подготовки из сообщения.</p>
 */
public final class SaveSpecialty implements Action<EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public SaveSpecialty(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final String text = context.message().text();
        assert text != null; // Otherwise it's not valid input

        userEntryStorage.changeUserEntrySpecialty(context.user().id(), text);
    }
}
