package ru.urfu;

import org.jetbrains.annotations.Nullable;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 * @param text текст сообщения.
 */
public record Message(@Nullable String text) {
    /**
     * Создаёт объект билдера для этого класса.
     * @return билдер для класса Message
     */
    public MessageBuilder builder() {
        // TODO: это метод бы сделать статичным.
        return new MessageBuilder();
    }
}
