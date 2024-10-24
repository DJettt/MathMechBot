package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.MMBCore;

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
public final class YesNoBackHandler implements MessageHandler {
    @Override
    public Event processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
            case Constants.BACK_COMMAND -> Event.BACK;
            case Constants.ACCEPT_COMMAND -> Event.ACCEPT;
            case Constants.DECLINE_COMMAND -> Event.DECLINE;
            case null, default -> Event.INVALID_INPUT;
        };
    }
}
