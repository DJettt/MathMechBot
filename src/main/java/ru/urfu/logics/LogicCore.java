package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.logics.localobjects.LocalMessage;

/**
 * <p>Логическое ядро -- объект, получающий сообщения с различных
 * платформ и определяющий действия, которые необходимо предпринять.</p>
 */
public interface LogicCore {
    /**
     * <p>Точка входа для ботов. Они должны вызывать
     * этот метод, чтобы обратиться к ядру.</p>
     *
     * @param id      id отправителя сообщения на платформе.
     * @param message сообщение.
     * @param bot     бот, принявший сообщение.
     */
    void processMessage(@NotNull Long id, @NotNull LocalMessage message, @NotNull Bot bot);
}
