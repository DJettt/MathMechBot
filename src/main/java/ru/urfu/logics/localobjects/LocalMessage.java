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
     * <p>Проверка на наличие кнопок.</p>
     *
     * @return результат проверки.
     */
    public boolean hasButtons() {
        return buttons != null && !buttons.isEmpty();
    }
}
