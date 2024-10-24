package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;

/**
 * <p>Контекст события в FSM. Содержит ряд полей,
 * которые могут потребоваться для совершения действия.</p>
 *
 * @param user    пользователь, который прислал сообщение.
 * @param message присланное сообщение.
 * @param bot     бот, которому нужно отправлять сообщения.
 */
public record EventContext(
        @NotNull User user,
        @NotNull LocalMessage message,
        @NotNull Bot bot
) {
}
