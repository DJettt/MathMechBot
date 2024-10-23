package ru.urfu.mathmechbot.messagehandlers;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.UserEntry;


/**
 * <p>Состояние, в котором изначально пребывает пользователь.</p>
 */
public final class DefaultStateHandler implements MMBMessageHandler {
    @Override
    public Event processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final Optional<UserEntry> userEntryOptional = request
                .context()
                .getStorage()
                .getUserEntries()
                .get(request.user().id());

        return switch (request.message().text()) {
            case Constants.REGISTER_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.ALREADY_REGISTERED;
                } else {
                    yield Event.REGISTER;
                }
            }
            case Constants.INFO_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.INFO;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case Constants.DELETE_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.DELETE;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case Constants.EDIT_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.EDIT;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case null, default -> Event.HELP;
        };
    }
}
