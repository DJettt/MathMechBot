package ru.urfu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро
 */
public record Message(@Nullable String text, @NotNull List<BufferedImage> images) {
    public MessageBuilder builder() {
        return new MessageBuilder();
    }
}
