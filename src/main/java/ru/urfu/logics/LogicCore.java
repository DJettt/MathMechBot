package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.BotProcessMessageRequest;

/**
 * <p>Логическое ядро -- объект, получающий сообщения с различны
 * платформ и определяющий действия, которые необходимо предпринять.</p>
 */
public interface LogicCore {
    /**
     * <p>Точка входа для ботов. Они должны вызывать
     * этот метод, чтобы обратиться к ядру.</p>
     *
     * @param request запрос.
     */
    void processMessage(@NotNull BotProcessMessageRequest request);
}
