package ru.urfu.logics.localobjects;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.logics.LogicCore;
import ru.urfu.mathmechbot.models.User;

/**
 * <p>Запрос обработать сообщение, который ядро-контекст
 * посылает отдельным своим состояниям.</p>
 *
 * <p>Использовать этот класс стоит только для отправки
 * сообщений от ядра-контекста отдельным состояниям.</p>
 *
 * @param context контекст.
 * @param user    пользователь, которому нужно отправлять сообщение.
 * @param message сообщение.
 * @param bot     бот, которому отправлять все сообщения.
 *
 * @param <T> тип логического ядра-отправителя.
 */
public record ContextProcessMessageRequest<T extends LogicCore>(
        @NotNull T context,
        @NotNull User user,
        @NotNull LocalMessage message,
        @NotNull Bot bot
) {
}
