package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBEvent;

/**
 * <p>Состояние в котором пользователь выбирает, какую информацию он хочет изменить.</p>
 */
public final class EditingChooseHandler implements MMBMessageHandler {
    @Override
    public MMBEvent processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
            case Constants.BACK_COMMAND -> MMBEvent.BACK;
            case Constants.EDITING_FULL_NAME -> MMBEvent.FULL_NAME_CHOSEN;
            case Constants.EDITING_YEAR -> MMBEvent.YEAR_CHOSEN;
            case Constants.EDITING_SPECIALITY -> MMBEvent.SPECIALTY_CHOSEN;
            case Constants.EDITING_GROUP -> MMBEvent.GROUP_CHOSEN;
            case Constants.EDITING_MEN -> MMBEvent.MEN_CHOSEN;
            case null, default -> MMBEvent.INVALID_INPUT;
        };
    }
}
