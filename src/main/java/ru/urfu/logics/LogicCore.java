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
     * <p>Решает, что делать с сообщениями. Боты вызывают этот метод.</p>
     *
     * @param chatId  id отправителя сообщения на платформе.
     * @param message сообщение.
     * @param bot     бот, принявший сообщение.
     */
    void processMessage(@NotNull Long chatId, @NotNull LocalMessage message, @NotNull Bot bot);
}
