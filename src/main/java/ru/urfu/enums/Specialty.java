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
    private final String abbreviation;
    /**
     * Конструктор элемента перечисления.
     * @param abbreviation аббревиатура направления
     */
    Specialty(@NotNull String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Геттер поля abbreviation.
     * @return значение abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }
}
