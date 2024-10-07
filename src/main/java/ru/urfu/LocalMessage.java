package ru.urfu;

import java.util.List;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 */
public class LocalMessage {
    private final String text;
    private final List<List<LocalButton>> buttons;

    /**
     * Конструктор по сообщению.
     * @param messageText текст сообщения
     */
    public LocalMessage(String messageText) {
        text = messageText;
        buttons = null;
    }

    /**
     * Перегрузка конструктора на случай если нужно будет передать информацию о кнопках в сообщении.
     * @param messageText текст сообщения
     * @param buttons все кнопки, которые должны присутствовать в сообщении
     */
    public LocalMessage(String messageText, List<List<LocalButton>> buttons) {
        text = messageText;
        this.buttons = buttons;
    }

    /**
     * Геттер поля MESSAGE_STATUS.
     * @return содержимое поля MESSAGE_STATUS
     */
    public boolean hasButtons() {
        return buttons != null;
    }

    /**
     * Геттер поля text.
     * @return содержимое поля text
     */
    public String getText() {
        return text;
    }

    /**
     * Геттер поля buttons.
     * @return содержимое поля buttons
     */
    public List<List<LocalButton>> getButtons() {
        return buttons;
    }
}
