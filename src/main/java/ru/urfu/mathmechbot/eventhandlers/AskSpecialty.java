package ru.urfu.mathmechbot.eventhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.logicstates.SpecialtyCheckState;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.UserEntry;

public final class AskSpecialty implements EventHandler<RequestEvent<MMBCore>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AskSpecialty.class);

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final Optional<UserEntry> userEntryOptional = e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User doesn't have user entry but managed to reach specialty state");
            return;
        }

        final UserEntry userEntry = userEntryOptional.get();
        if (userEntry.year() == null) {
            LOGGER.error("User entry doesn't contain year but it should");
            return;
        }

        final List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : new SpecialtyCheckState().getAllowedSpecialties(userEntry.year())) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(Constants.BACK_BUTTON);

        final LocalMessage message = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(buttons)
                .build();

        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
