package ru.urfu.logics.localobjects;

import java.util.List;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Абстракция над сообщениями на различных платформах.</p>
 *
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
