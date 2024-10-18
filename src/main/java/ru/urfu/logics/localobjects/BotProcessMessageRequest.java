package ru.urfu.logics.localobjects;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;

/**
 * <p>>Запрос на обработку сообщения, посылаемый ботом ядру.</p>
 *
 * <p>Использовать этот класс стоит только для отправки сообщений от бота к ядру.</p>
 *
 * @param id      внутренний id отправителя сообщения.
 * @param message сообщение.
 * @param bot     бот, принявший сообщение.
 */
public record BotProcessMessageRequest(
        long id,
        @NotNull LocalMessage message,
        @NotNull Bot bot
) {
}
