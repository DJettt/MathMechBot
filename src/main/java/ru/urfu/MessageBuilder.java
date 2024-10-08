package ru.urfu;

import org.jetbrains.annotations.Nullable;

/**
 * Билдер для класса Message.
 */
public final class MessageBuilder {
    private String text;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     */
    public MessageBuilder() {
        text = null;
    }

    /**
     * Устанавливает поле text будущего объекта.
     * @param text строка, которую нужно положить в text
     * @return себя же
     */
    public MessageBuilder text(@Nullable String text) {
        this.text = text;
        return this;
    }

    /**
     * Создаёт объект Message.
     * @return созданный объект
     */
    public Message build() {
        return new Message(text);
    }
}
