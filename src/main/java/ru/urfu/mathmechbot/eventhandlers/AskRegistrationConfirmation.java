package ru.urfu.mathmechbot.eventhandlers;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * <p>Спрашивает у пользователя подтверждение регистрации.</p>
 */
public final class AskRegistrationConfirmation
        implements EventHandler<RequestEvent<MMBCore>> {

    private final Logger logger = LoggerFactory
            .getLogger(AskRegistrationConfirmation.class);

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final Optional<UserEntry> userEntryOptional = e
                .request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry reached registration end");
            return;
        }

        final LocalMessage message = new LocalMessageBuilder()
                .text(Constants.CONFIRM_PREFIX + userEntryOptional
                        .get()
                        .toHumanReadable())
                .buttons(List.of(
                        Constants.YES_BUTTON,
                        Constants.NO_BUTTON,
                        Constants.BACK_BUTTON))
                .build();
        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
