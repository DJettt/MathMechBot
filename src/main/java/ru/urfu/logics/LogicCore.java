package ru.urfu.logics;

import ru.urfu.Message;

/**
 * Абстрактный класс логического ядра.
 */
public interface LogicCore {
    /**
     * Обработчик сообщений, возвращающий ответы на них.
     * @param msg сообщение, которое нужно обработать
     * @return ответ на сообщение (null, если ответа нет)
     */
    Message processMessage(Message msg);
}
