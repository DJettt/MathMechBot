package ru.urfu;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Билдер для класса Message.
 */
public final class MessageBuilder {
    private String text;
    private List<BufferedImage> images;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     */
    public MessageBuilder() {
        text = null;
        images = new ArrayList<>();
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
     * Устанавливает поле images будущего объекта.
     * @param images список изображений, который нужно положить в images
     * @return себя же
     */
    public MessageBuilder images(@NotNull List<BufferedImage> images) {
        this.images = images;
        return this;
    }

    /**
     * Создаёт объект Message.
     * @return созданный объект
     */
    public Message build() {
        return new Message(text, images);
    }
}
