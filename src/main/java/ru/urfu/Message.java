package ru.urfu;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро
 */
public class Message {
    private final String text;
    // TODO: возможность обрабатывать всякие приложения типа фото, музыки и так далее.

    /** Конструктор
     * @param messageText текст сообщения
     */
    public Message(String messageText) {
        text = messageText;
    }

    /**
     * Геттер поля text
     * @return содержимое поля text
     */
    public String getText() {
        return text;
    }
}
