package ru.urfu.mathmechbot.timetable;

import java.time.LocalDate;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Расписание на день.</p>
 *
 * @param date    дата расписания.
 * @param lessons занятия на неделе.
 */
public record DailyTimetable(
        @NotNull LocalDate date,
        @NotNull List<Lesson> lessons
) {
}
