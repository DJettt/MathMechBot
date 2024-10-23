package ru.urfu.mathmechbot.messagehandlers;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBEvent;
import ru.urfu.mathmechbot.models.UserEntry;


/**
 * <p>Состояние, в котором изначально пребывает пользователь.</p>
 */
public final class DefaultStateHandler implements MMBMessageHandler {
    @Override
    public MMBEvent processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final Optional<UserEntry> userEntryOptional = request
                .context()
                .getStorage()
                .getUserEntries()
                .get(request.user().id());

        return switch (request.message().text()) {
            case Constants.REGISTER_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield MMBEvent.ALREADY_REGISTERED;
                } else {
                    yield MMBEvent.REGISTER;
                }
            }
            case Constants.INFO_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield MMBEvent.INFO;
                } else {
                    yield MMBEvent.NOT_REGISTERED;
                }
            }
            case Constants.DELETE_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield MMBEvent.DELETE;
                } else {
                    yield MMBEvent.NOT_REGISTERED;
                }
            }
            case Constants.EDIT_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield MMBEvent.EDIT;
                } else {
                    yield MMBEvent.NOT_REGISTERED;
                }
            }
            case null, default -> MMBEvent.HELP;
        };
    }
}
