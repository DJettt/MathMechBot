package ru.urfu.localobjects;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;

/**
 * Запрос, посылаемый ботом ядру.
 *
 * @param id      внутренний id отправителя сообщения.
 * @param message сообщение.
 * @param bot     бот, принявший сообщение.
 */
public record Request(
        long id,
        @NotNull LocalMessage message,
        @NotNull Bot bot
) {
}
