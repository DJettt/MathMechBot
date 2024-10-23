package ru.urfu.mathmechbot.actions;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.UserEntryStorage;

/**
 * <p>Спрашивает у пользователя подтверждение удаления информации.</p>
 */
public final class AskDeletionConfirmation implements MMBAction {
    private final Logger logger = LoggerFactory.getLogger(AskDeletionConfirmation.class);
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для чтения.
     */
    public AskDeletionConfirmation(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final UserEntry userEntry = userEntryStorage
                .get(context.user().id())
                .orElseThrow(() -> {
                    logger.error("User without entry reached deletion confirmation state");
                    return new RuntimeException();
                });

        final LocalMessage message = new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userEntry.toHumanReadable())
                .buttons(List.of(
                        Constants.YES_BUTTON,
                        Constants.NO_BUTTON,
                        Constants.BACK_BUTTON))
                .build();
        context.bot().sendMessage(message, context.user().id());
    }
}
