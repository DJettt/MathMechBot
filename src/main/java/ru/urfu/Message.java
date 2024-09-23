package ru.urfu;

public class Message {
    private final String text;
    // TODO: возможность обрабатывать всякие приложения типа фото, музыки и так далее.

    public Message(String messageText) {
        text = messageText;
    }

    public String getText() {
        return text;
    }
}
