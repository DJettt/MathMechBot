package ru.urfu;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро
 */
public class LocalMessage {
    private final String text;
    // TODO: возможность обрабатывать всякие приложения типа фото, музыки и так далее.

    /**
     * @param messageText текст сообщения
     */
    public LocalMessage(String messageText) {
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
