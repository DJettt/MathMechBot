package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBEvent;

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
public final class YesNoBackHandler implements MMBMessageHandler {
    @Override
    public MMBEvent processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
            case Constants.BACK_COMMAND -> MMBEvent.BACK;
            case Constants.ACCEPT_COMMAND -> MMBEvent.ACCEPT;
            case Constants.DECLINE_COMMAND -> MMBEvent.DECLINE;
            case null, default -> MMBEvent.INVALID_INPUT;
        };
    }
}
