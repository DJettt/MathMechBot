package ru.urfu;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро
 */
public record Message(String text, @NotNull List<File> images) {
    public MessageBuilder builder() {
        return new MessageBuilder();
    }
}
