package ru.urfu.mathmechbot.logicstates;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.events.AcceptEvent;
import ru.urfu.mathmechbot.events.BackEvent;
import ru.urfu.mathmechbot.events.DeclineEvent;
import ru.urfu.mathmechbot.events.InvalidInputEvent;

/**
 * <p>Состояние логического ядра, обрабатывающее четыре возможных типа сообщения.</p>
 *
 * <ul>
 *     <li>Команда согласия -- отправляет в FSM AcceptEvent</li>
 *     <li>Команда несогласия -- отправляет в FSM DeclineEvent</li>
 *     <li>Команда назад -- отправляет в FSM BackEvent</li>
 *     <li>Всё остальное -- отправляет в FSM InvalidInputEvent</li>
 * </ul>
 */
public final class YesNoBackState implements MMBCoreState {
    @Override
    public void processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final RequestEvent<MMBCore> event = switch (request.message().text()) {
            case Constants.BACK_COMMAND -> new BackEvent(request);
            case Constants.ACCEPT_COMMAND -> new AcceptEvent(request);
            case Constants.DECLINE_COMMAND -> new DeclineEvent(request);
            case null, default -> new InvalidInputEvent(request);
        };
        request.context().getFsm().dispatch(event);
    }
}
