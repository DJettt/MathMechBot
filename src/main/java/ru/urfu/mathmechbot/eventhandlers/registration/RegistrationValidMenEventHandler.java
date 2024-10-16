package ru.urfu.mathmechbot.eventhandlers.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * Обрабатывает корректный ввод МЕН-группы.
 */
public final class RegistrationValidMenEventHandler implements EventHandler<ValidInputEvent> {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationValidMenEventHandler.class);

    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryMen(e.request().user().id(), text.trim());

        final Optional<UserEntry> userEntryOptional = e
                .request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry reached registration end");
            return;
        }

        final LocalMessage message = new LocalMessageBuilder()
                .text(Constants.CONFIRM_PREFIX + userEntryOptional.get().toHumanReadable())
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
