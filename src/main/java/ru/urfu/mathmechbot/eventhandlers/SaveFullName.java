package ru.urfu.mathmechbot.eventhandlers;

import java.util.List;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;

/**
 * Обрабатывает корректный ввод ФИО во время редактирования.
 */
public final class SaveFullName implements EventHandler<RequestEvent<MMBCore>> {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        final String trimmedText = text.trim();
        final List<String> strings = List.of(trimmedText.split("\\s+"));

        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;
        final UserEntry currentUserEntry = e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .orElseThrow();
        final UserEntry updatedUserEntry =
                new UserEntryBuilder(currentUserEntry)
                        .surname(strings.get(0))
                        .name(strings.get(1))
                        .patronym((hasPatronym) ? strings.get(2) : null)
                        .build();

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .update(updatedUserEntry);
    }
}
