package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;

/**
 * <p>Состояние в котором пользователь выбирает, какую информацию он хочет изменить.</p>
 */
public final class EditingChooseHandler implements MessageHandler {
    @NotNull
    @Override
    public Event processMessage(@NotNull MathMechStorageInterface storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        return switch (message.text()) {
            case Constants.BACK_COMMAND -> Event.BACK;
            case Constants.EDITING_FULL_NAME -> Event.FULL_NAME_CHOSEN;
            case Constants.EDITING_YEAR -> Event.YEAR_CHOSEN;
            case Constants.EDITING_SPECIALITY -> Event.SPECIALTY_CHOSEN;
            case Constants.EDITING_GROUP -> Event.GROUP_CHOSEN;
            case Constants.EDITING_MEN -> Event.MEN_CHOSEN;
            case null, default -> Event.INVALID_INPUT;
        };
    }
}
