package ru.urfu.mathmechbot.messagehandlers;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.MathMechStorage;


/**
 * <p>Состояние, в котором изначально пребывает пользователь.</p>
 */
public final class DefaultStateHandler implements MessageHandler {
    @Override
    public Event processMessage(@NotNull MathMechStorage storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        final Optional<UserEntry> userEntryOptional = storage
                .getUserEntries()
                .get(user.id());

        return switch (message.text()) {
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
