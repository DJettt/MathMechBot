package ru.urfu.mathmechbot.eventhandlers.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.eventhandlers.GenericInvalidInputEventHandler;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * Обрабатывает некорректный ввод во время подтверждения регистрации.
 */
public final class RegistrationInvalidConfirmEventHandler extends GenericInvalidInputEventHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationInvalidConfirmEventHandler.class);

    @Override
    public void handleEvent(InvalidInputEvent e) {
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

        super.handleEvent(e);
        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
