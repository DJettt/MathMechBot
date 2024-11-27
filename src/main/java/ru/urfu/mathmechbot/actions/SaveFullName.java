package ru.urfu.mathmechbot.actions;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Сохраняет ФИО из сообщения.</p>
 */
public final class SaveFullName implements Action<EventContext> {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public SaveFullName(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final String text = context.message().text();
        assert text != null; // Otherwise it's not valid input

        final String trimmedText = text.trim();
        final List<String> strings = List.of(trimmedText.split("\\s+"));

        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;
        final UserEntry currentUserEntry = userEntryStorage
                .get(context.user().id())
                .orElseThrow();

        final UserEntry updatedUserEntry = new UserEntryBuilder(currentUserEntry)
                .surname(strings.get(0))
                .name(strings.get(1))
                .patronym((hasPatronym) ? strings.get(2) : null)
                .build();
        userEntryStorage.update(updatedUserEntry);
    }
}
