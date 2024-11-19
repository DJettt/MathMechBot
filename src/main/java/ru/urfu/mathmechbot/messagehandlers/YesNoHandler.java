package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;

/**
 * <p>Состояние логического ядра, обрабатывающее три возможных типа сообщения.</p>
 *
 * <ul>
 *     <li>Команда согласия -- возвращает AcceptEvent</li>
 *     <li>Команда несогласия -- возвращает DeclineEvent</li>
 *     <li>Всё остальное -- возвращает InvalidInputEvent</li>
 * </ul> */
public final class YesNoHandler implements MessageHandler {
    @NotNull
    @Override
    public Event processMessage(@NotNull MathMechStorageInterface storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        return switch (message.text()) {
            case Constants.ACCEPT_COMMAND -> Event.ACCEPT;
            case Constants.DECLINE_COMMAND -> Event.DECLINE;
            case null, default -> Event.INVALID_INPUT;
        };
    }
}
