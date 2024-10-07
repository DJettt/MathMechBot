package ru.urfu.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.Specialty;

/**
 * Модель введённых пользователем данных.
 * @param id идентификатор введённых данных.
 * @param name имя человека, упоминания которого мы ищем.
 * @param surname фамилия того же.
 * @param patronym отчество того же (если есть).
 * @param specialty направление подготовки того же человека.
 * @param year курс.
 * @param group номер группы того же человека;
 * @param userId идентификатор модели User того, указывающий на того, кому пересылать упоминания этого человека.
 */
public record UserEntry(
        Long id,
        @NotNull String name,
        @NotNull String surname,
        @Nullable String patronym,
        @NotNull Specialty specialty,
        int year,
        int group,
        Long userId
) implements Identifiable<Long> {
}
