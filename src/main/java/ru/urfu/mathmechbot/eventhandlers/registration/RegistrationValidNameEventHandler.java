package ru.urfu.mathmechbot.eventhandlers.registration;

import java.util.List;
import ru.urfu.fsm.EventHandler;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;

/**
 * Обрабатывает корректный ввод ФИО.
 */
public final class RegistrationValidNameEventHandler implements EventHandler<ValidInputEvent> {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        final String trimmedText = text.trim();
        final List<String> strings = List.of(trimmedText.split("\\s+"));

        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;
        final UserEntry newUserEntry = new UserEntryBuilder(
                e.request().user().id(), strings.get(0), strings.get(1), e.request().user().id())
                .patronym((hasPatronym) ? strings.get(2) : null)
                .build();

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .add(newUserEntry);

        e.request().bot().sendMessage(Constants.YEAR, e.request().user().id());
    }
}
