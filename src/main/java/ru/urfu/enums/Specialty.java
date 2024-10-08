package ru.urfu.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Перечисления всех направлений на МатМехе.
 */
public enum Specialty {
    KNMO("КНМО"),
    MMP("ММП"),
    KB("КБ"),
    FT("ФТ"),
    MH("МХ"),
    MT("МТ"),
    PM("ПМ"),
    MO("МО"),
    KN("КН");

    /**
     * Конструктор элемента перечисления.
     * @param abbreviation аббревиатура направления
     */
    Specialty(@NotNull String abbreviation) {
    }
}
