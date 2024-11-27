package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Перечисления всех направлений на МатМехе.</p>
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
     * <p>Конструктор элемента перечисления.</p>
     *
     * @param abbreviation аббревиатура направления
     */
    Specialty(@NotNull String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * <p>Геттер поля abbreviation.</p>
     *
     * @return содержимое поля.
     */
    @NotNull
    public String getAbbreviation() {
        return abbreviation;
    }
}
