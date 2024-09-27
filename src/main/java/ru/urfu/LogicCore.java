package ru.urfu;

/**
 * Абстрактный класс логического ядра
 */
public abstract class LogicCore {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * В процессе обработки может вызывать методы ботов, чтобы, например, отправлять сообщения.
     * @param msg сообщение, которое нужно обработать
     * @param chatId id чата, от кого пришло сообщение
     * @param bot бот, который получил сообщение
     * @return ответ на сообщение (null, если ответа нет)
     */
    public abstract Message processMessage(Message msg, Long chatId, Bot bot);

}
