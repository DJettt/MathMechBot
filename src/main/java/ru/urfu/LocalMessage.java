package ru.urfu;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро
 */
public class LocalMessage {
    private final String text;
    private final String MESSAGE_STATUS;
    private final List<ArrayList<LocalButton>> buttons;
    // TODO: возможность обрабатывать всякие приложения типа фото, музыки и так далее.

    /**
     * @param messageText текст сообщения
     */
    public LocalMessage(String messageText) {
        text = messageText;
        //TODO: костыль, чтобы Discord бот продолжал работать
        MESSAGE_STATUS = "message";
        buttons = null;
    }
    /**
     * @param messageText текст сообщения
     * @param status характеристика сообщения
     */
    public LocalMessage(String messageText, String status) {
        text = messageText;
        MESSAGE_STATUS = status;
        buttons = null;
    }

    /**
     * Перегрузка конструктора на случай если нужно будет передать информацию о кнопках в сообщении.
     * @param messageText текст сообщения
     * @param status статус сообщения, которое сгенерировал Core
     * @param btns все кнопки, которые должны присутствовать в сообщении
     */
    public LocalMessage(String messageText, String status, List<ArrayList<LocalButton>> btns) {
        text = messageText;
        MESSAGE_STATUS = status;
        buttons = btns;
    }

    /**
     * Геттер поля MESSAGE_STATUS
     * @return содержимое поля MESSAGE_STATUS
     */
    public String getStatus(){ return MESSAGE_STATUS; }
    /**
     * Геттер поля text
     * @return содержимое поля text
     */
    public String getText() {
        return text;
    }

    /**
     * Геттер поля buttons
     * @return содержимое поля buttons
     */
    public List<ArrayList<LocalButton>> getButtons(){ return buttons; }
}