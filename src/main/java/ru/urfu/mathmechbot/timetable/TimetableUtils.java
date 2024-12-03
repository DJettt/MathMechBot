package ru.urfu.mathmechbot.timetable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Утилиты для работы с расписанием.</p>
 */
public final class TimetableUtils {
    private final Locale ruLocale = new Locale.Builder()
            .setLanguage("ru").setRegion("RU").build();

    /**
     * <p>Определяет номер недели переданной даты.</p>
     *
     * @param date дата.
     * @return номер недели.
     */
    public int getWeekNumber(@NotNull LocalDate date) {
        return date.get(WeekFields.of(ruLocale).weekOfYear());
    }

    /**
     * <p>Возвращает дату понедельника недели, на которой расположена переданная дата.</p>
     *
     * @param date день, начало недели которого ищем.
     * @return дату понедельника.
     */
    @NotNull
    public LocalDate getWeekStartDate(@NotNull LocalDate date) {
        LocalDate monday = date;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        return monday;
    }

    /**
     * <p>Возвращает дату воскресенья недели, на которой расположена переданная дата.</p>
     *
     * @param date день, конец недели которого ищем.
     * @return дату воскресенья.
     */
    @NotNull
    public LocalDate getWeekEndDate(@NotNull LocalDate date) {
        LocalDate sunday = date;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return sunday;
    }
}