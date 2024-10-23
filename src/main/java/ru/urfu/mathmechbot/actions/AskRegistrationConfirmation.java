package ru.urfu.mathmechbot.actions;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.Action;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.UserEntryStorage;

/**
 * <p>Спрашивает у пользователя подтверждение регистрации.</p>
 */
public final class AskRegistrationConfirmation implements Action<EventContext> {
    private final Logger logger = LoggerFactory
            .getLogger(AskRegistrationConfirmation.class);
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для чтения.
     */
    public AskRegistrationConfirmation(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final UserEntry userEntry = userEntryStorage
                .get(context.user().id())
                .orElseThrow(() -> {
                    logger.error("User without entry reached registration end");
                    return new RuntimeException();
                });

        final LocalMessage message = new LocalMessageBuilder()
                .text(Constants.CONFIRM_PREFIX + userEntry.toHumanReadable())
                .buttons(List.of(
                        Constants.YES_BUTTON,
                        Constants.NO_BUTTON,
                        Constants.BACK_BUTTON))
                .build();
        context.bot().sendMessage(message, context.user().id());
    }
}