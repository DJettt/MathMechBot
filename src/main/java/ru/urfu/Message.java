package ru.urfu;

import java.awt.image.BufferedImage;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 * @param text текст сообщения.
 * @param images список изображений.
 */
public record Message(@Nullable String text, @NotNull List<BufferedImage> images) {
    /**
     * Создаёт объект билдера для этого класса.
     * @return билдер для класса Message
     */
    public MessageBuilder builder() {
        // TODO: это метод бы сделать статичным.
        return new MessageBuilder();
    }
}
