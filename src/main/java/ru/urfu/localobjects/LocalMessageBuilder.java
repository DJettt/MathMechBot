package ru.urfu.localobjects;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Билдер для класса LocalMessage.
 */
public final class LocalMessageBuilder {
    private String text;
    private List<LocalButton> buttons;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     */
    public LocalMessageBuilder() {
        text = null;
        buttons = null;
    }

    /**
     * Устанавливает поле text будущего объекта.
     * @param text строка, которую нужно положить в text
     * @return себя же
     */
    public LocalMessageBuilder text(@Nullable String text) {
        this.text = text;
        return this;
    }

    /**
     * Устанавливает поле buttons будущего объекта.
     * @param buttons все кнопки которые нужно отправить вместе с сообщением.
     * @return себя же
     */
    public LocalMessageBuilder buttons(@Nullable List<LocalButton> buttons) {
        this.buttons = buttons;
        return this;
    }

    /**
     * Создаёт объект LocalMessage.
     * @return созданный объект
     */
    public LocalMessage build() {
        return new LocalMessage(text, buttons);
    }
}
