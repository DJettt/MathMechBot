package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.storages.Identifiable;

/**
 * <p>Модель введённых пользователем данных.</p>
 *
 * @param id идентификатор введённых данных.
 * @param name имя человека, упоминания которого мы ищем.
 * @param surname фамилия того же.
 * @param patronym отчество того же (если есть).
 * @param specialty направление подготовки того же человека.
 * @param men академическая группа в формате МЕН.
 * @param year курс.
 * @param group номер группы того же человека;
 * @param userId идентификатор модели User того, указывающий
 *               на того, кому пересылать упоминания этого человека.
 */
public record UserEntry(
        @NotNull Long id,
        @Nullable String surname,
        @Nullable String name,
        @Nullable String patronym,
        @Nullable String specialty,
        @Nullable String men,
        @Nullable Integer year,
        @Nullable Integer group,
        @NotNull Long userId
) implements Identifiable<Long> {
    /**
     * <p>Представляет объект UserEntry в виде
     * удобного для восприятия пользователем текста.</p>
     *
     * @return строка с содержимым объекта в удобном для пользователя виде.
     */
    public String toHumanReadable() {
        return "ФИО: %s\nГруппа: %s-%d0%d (%s)".formatted(
                surname + " " + name + ((patronym != null) ? " " + patronym : ""),
                specialty, year, group, men
        );
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
