package ru.urfu.mathmechbot.actions;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.Action;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.Utils;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;

/**
 * <p>Отправляет пользователю сообщение с вариантами
 * выбора направления подготовки (на основе года обучения).</p>
 */
public final class AskSpecialty implements Action<EventContext> {
    private final Logger logger = LoggerFactory.getLogger(AskSpecialty.class);
    private final Utils utils = new Utils();

    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для чтения.
     */
    public AskSpecialty(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final UserEntry userEntry = userEntryStorage
                .get(context.user().id())
                .orElseThrow(() -> {
                    logger.error("User entry doesn't contain year but it should");
                    return new RuntimeException();
                });

        assert userEntry.year() != null;

        final List<LocalButton> buttons = new ArrayList<>(utils
                .getAllowedSpecialties(userEntry.year())
                .stream()
                .map(specialty -> {
                    final String abbr = specialty.getAbbreviation();
                    return new LocalButton(abbr, abbr);
                })
                .toList());

        buttons.add(utils.makeBackButton());

        final LocalMessage message = new LocalMessageBuilder()
                .text("""
                        На каком направлении?
                        Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.""")
                .buttons(buttons)
                .build();

        context.bot().sendMessage(message, context.user().id());
    }
}
