package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.events.BackEvent;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.events.editing.FullNameChosenEvent;
import ru.urfu.mathmechbot.events.editing.GroupChosenEvent;
import ru.urfu.mathmechbot.events.editing.MenChosenEvent;
import ru.urfu.mathmechbot.events.editing.SpecialtyChosenEvent;
import ru.urfu.mathmechbot.events.editing.YearChosenEvent;

/**
 * <p>Состояние в котором пользователь выбирает, какую информацию он хочет изменить.</p>
 */
public final class EditingChooseHandler implements MMBMessageHandler {
    @Override
    public RequestEvent<MMBCore> processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
            case Constants.BACK_COMMAND -> new BackEvent(request);
            case Constants.EDITING_FULL_NAME -> new FullNameChosenEvent(request);
            case Constants.EDITING_YEAR -> new YearChosenEvent(request);
            case Constants.EDITING_SPECIALITY -> new SpecialtyChosenEvent(request);
            case Constants.EDITING_GROUP -> new GroupChosenEvent(request);
            case Constants.EDITING_MEN -> new MenChosenEvent(request);
            case null, default -> new InvalidInputEvent(request);
        };
    }
}
