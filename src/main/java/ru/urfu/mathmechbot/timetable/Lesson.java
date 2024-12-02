package ru.urfu.mathmechbot.timetable;

import java.time.LocalTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Занятие университета.</p>
 *
 * @param name     название занятия.
 * @param lector   преподаватель.
 * @param time     время проведения.
 * @param location место проведения.
 */
public record Lesson(
        @NotNull String name,
        @Nullable String lector,
        @NotNull LocalTime time,
        @Nullable String location
) {
}
