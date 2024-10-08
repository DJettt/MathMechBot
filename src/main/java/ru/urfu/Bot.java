package ru.urfu;

/**
 * Интерефейс бота, описывающий методы, которые ожидаются в каждом боте.
 */
public interface Bot {
    /**
     * Запускает бота.
     */
    void start();

    /**
     * Отправляет сообщение.
     * @param msg сообщение, которое требуется отправить
     * @param id идентификатор получателя
     */
    void sendMessage(Message msg, Long id);
}
