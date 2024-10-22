package ru.urfu.bots;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;

/**
 * <p>Интерфейс бота, описывающий методы, которые ожидаются в каждом боте.</p>
 */
public interface Bot {
    /**
     * <p>Отправляет сообщение.</p>
     *
     * @param msg сообщение, которое требуется отправить
     * @param id идентификатор получателя
     */
    void sendMessage(@NotNull LocalMessage msg, @NotNull Long id);
}
