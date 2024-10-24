package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Состояние в котором пользователь выбирает, какую информацию он хочет изменить.</p>
 */
public final class EditingChooseHandler implements MessageHandler {
    @Override
    public Event processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
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
