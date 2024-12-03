package ru.urfu.mathmechbot.messagehandlers;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.MathMechStorage;


/**
 * <p>Состояние, в котором изначально пребывает пользователь.</p>
 */
public final class DefaultStateHandler implements MessageHandler {
    public final static String REGISTER_COMMAND = "/register";
    public final static String INFO_COMMAND = "/info";
    public final static String EDIT_COMMAND = "/edit";
    public final static String DELETE_COMMAND = "/delete";
    public final static String TIMETABLE_COMMAND = "/timetable";

    @NotNull
    @Override
    public Event processMessage(@NotNull MathMechStorage storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        final Optional<UserEntry> userEntryOptional = storage
                .getUserEntries()
                .get(user.id());

        return switch (message.text()) {
            case REGISTER_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.ALREADY_REGISTERED;
                } else {
                    yield Event.REGISTER;
                }
            }
            case INFO_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.INFO;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case DELETE_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.DELETE;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case EDIT_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.EDIT;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case TIMETABLE_COMMAND -> {
                if (userEntryOptional.isPresent()) {
                    yield Event.TIMETABLE;
                } else {
                    yield Event.NOT_REGISTERED;
                }
            }
            case null, default -> Event.HELP;
        };
    }
}
