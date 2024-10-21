package ru.urfu.localobjects;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 * @param text текст сообщения.
 * @param buttons кнопки сообщения.
 */
public record LocalMessage(@Nullable String text, @Nullable List<LocalButton> buttons) {

    /**
     * Конструктор LocalMessage с одной переменной.
     * @param text строка, которую нужно сохранить.
     */
    public LocalMessage(@NotNull String text) {
        this(text, null);
    }

    /**
     * Проверка на наличие кнопок.
     * @return есть ли кнопки или нет.
     */
    public boolean hasButtons() {
        return buttons != null && !buttons.isEmpty();
    }
}
