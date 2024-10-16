package ru.urfu.mathmechbot.logicstates;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.events.AlreadyRegisteredEvent;
import ru.urfu.mathmechbot.events.DeleteEvent;
import ru.urfu.mathmechbot.events.EditEvent;
import ru.urfu.mathmechbot.events.HelpEvent;
import ru.urfu.mathmechbot.events.InfoEvent;
import ru.urfu.mathmechbot.events.NotRegisteredEvent;
import ru.urfu.mathmechbot.events.RegisterEvent;
import ru.urfu.mathmechbot.models.UserEntry;


/**
 * Состояние, в котором изначально пребывает пользователь.
 */
public final class DefaultState implements MMBCoreState {
    @Override
    public void processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final Optional<UserEntry> userEntryOptional = request
                .context()
                .getStorage()
                .getUserEntries()
                .get(request.user().id());

        final RequestEvent<MMBCore> event = switch (request.message().text()) {
            case Constants.REGISTER_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield new AlreadyRegisteredEvent(request);
                } else {
                    yield new RegisterEvent(request);
                }
            }
            case Constants.INFO_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield new InfoEvent(request);
                } else {
                    yield new NotRegisteredEvent(request);
                }
            }
            case Constants.DELETE_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield new DeleteEvent(request);
                } else {
                    yield new NotRegisteredEvent(request);
                }
            }
            case Constants.EDIT_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield new EditEvent(request);
                } else {
                    yield new NotRegisteredEvent(request);
                }
            }
            case null, default -> new HelpEvent(request);
        };

        request.context().getFsm().dispatch(event);
    }
}
