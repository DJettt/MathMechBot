package ru.urfu.localobjects;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 * @param text текст сообщения.
 */
public record LocalMessage(@Nullable String text, @Nullable List<LocalButton> buttons) {

    /**
     * Проверка на наличие кнопок.
     * @return есть ли кнопки или нет.
     */
    public boolean hasButtons() {
        return buttons != null;
    }
    /**
     * Создаёт объект билдера для этого класса.
     * @return билдер для класса LocalMessage
     */
    public LocalMessageBuilder builder() {
        // TODO: это метод бы сделать статичным.
        return new LocalMessageBuilder();
    }
}
