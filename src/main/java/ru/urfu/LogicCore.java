package ru.urfu;

/**
 * Абстрактный класс логического ядра
 */
public abstract class LogicCore {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * @param msg сообщение, которое нужно обработать
     * @return ответ на сообщение (null, если ответа нет)
     */
    public abstract LocalMessage processMessage(LocalMessage msg);
}
