package ru.urfu.logics.localobjects;

import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * Класс сообщения, объектами которого общаются боты и логическое ядро.
 * @param text текст сообщения.
 * @param buttons кнопки сообщения.
 */
public record LocalMessage(@Nullable String text, @Nullable List<LocalButton> buttons) {

    /**
     * Проверка на наличие кнопок.
     * @return есть ли кнопки или нет.
     */
    public boolean hasButtons() {
        return buttons != null && !buttons.isEmpty();
    }
}
