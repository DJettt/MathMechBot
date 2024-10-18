package ru.urfu.logics.localobjects;

import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Билдер для класса LocalMessage.</p>
 */
public final class LocalMessageBuilder {
    private String text;
    private List<LocalButton> buttons;

    /**
     * <p>Конструктор, устанавливающий дефолтные значения для полей.</p>
     */
    public LocalMessageBuilder() {
        text = null;
        buttons = null;
    }

    /**
     * <p>Устанавливает поле text будущего объекта.</p>
     *
     * @param text строка, которую нужно положить в text
     * @return себя же
     */
    public LocalMessageBuilder text(@Nullable String text) {
        this.text = text;
        return this;
    }

    /**
     * <p>Устанавливает поле buttons будущего объекта.</p>
     *
     * @param buttons все кнопки которые нужно отправить вместе с сообщением.
     * @return себя же
     */
    public LocalMessageBuilder buttons(@Nullable List<LocalButton> buttons) {
        this.buttons = buttons;
        return this;
    }

    /**
     * <p>Создаёт объект LocalMessage.</p>
     *
     * @return созданный объект
     */
    public LocalMessage build() {
        return new LocalMessage(text, buttons);
    }
}
