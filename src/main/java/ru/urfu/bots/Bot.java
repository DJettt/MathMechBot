package ru.urfu.bots;

import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;

/**
 * Интерфейс бота, описывающий методы, которые ожидаются в каждом боте.
 */
public interface Bot {
    /**
     * Отправляет сообщение.
     * @param msg сообщение, которое требуется отправить
     * @param id идентификатор получателя
     */
    void sendMessage(@NotNull LocalMessage msg, @NotNull Long id);
}
