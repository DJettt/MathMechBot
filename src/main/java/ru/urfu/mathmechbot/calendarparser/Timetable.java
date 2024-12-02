package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Расписание пар на текущую неделю.
 */
public class Timetable {
    private final List<DatedTimetable> timetable = new ArrayList<>();

    /**
     * Конструктор. Создает 7 дневных расписаний в виде списка.
     * @param date дата, находящаяся в текущей неделе.
     */
    //TODO избавиться от заглушки warning-а
    @SuppressWarnings("MagicNumber")
    Timetable(LocalDate date) {
        LocalDate mondayDate = findMonday(date);
        timetable.add(new DatedTimetable(mondayDate));
        timetable.add(new DatedTimetable(mondayDate.plusDays(1)));
        timetable.add(new DatedTimetable(mondayDate.plusDays(2)));
        timetable.add(new DatedTimetable(mondayDate.plusDays(3)));
        timetable.add(new DatedTimetable(mondayDate.plusDays(4)));
        timetable.add(new DatedTimetable(mondayDate.plusDays(5)));
        timetable.add(new DatedTimetable(mondayDate.plusDays(6)));
    }

    /**
     * Ищет дату понедельника на неделе с текущей датой.
     * @param date текущая дата
     * @return дату понедельника
     */
    //TODO избавиться от заглушки warning-а
    @SuppressWarnings("ParameterAssignment")
    private LocalDate findMonday(LocalDate date) {
        while (date.getDayOfWeek().getValue() != 1) {
            date = date.minusDays(1);
        }
        return date;
    }

    /**
     * Добавляет пару в расписание на неделю.
     * @param lesson пара
     * @param date дата пары
     * @param time время начала пары
     */
    public void add(Lesson lesson, LocalDate date, LocalTime time) {
        for (DatedTimetable datedTimetable : timetable) {
            if (date.equals(datedTimetable.getDate())) {
                datedTimetable.add(lesson, time);
                return;
            }
        }
    }

    /**
     * Возвращает дневное расписание по дате.
     * @param date дата
     * @return искомое расписание дня
     */
    public List<List<Lesson>> getByDate(LocalDate date) {
        for (DatedTimetable datedTimetable : timetable) {
            if (date.equals(datedTimetable.getDate())) {
                return datedTimetable.getAll();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Возвращает Optional строки с дневным расписанием по искомому дню.
     * @param date день
     * @return Optional по строке с расписанием
     */
    public Optional<String> showByDate(LocalDate date) {
        StringBuilder result = new StringBuilder();
        for (DatedTimetable datedTimetable : timetable) { //зашли в день недели
            if (date.equals(datedTimetable.getDate())) {
                result.append(datedTimetable.showDatedTimetable()).append("\n");
            }
        }
        return Optional.of(result.toString());
    }

    /**
     * Возвращает Optional по строке с расписанием на всю текущую неделю.
     * @return Optional по строке с расписанием
     */
    public Optional<String> showTimetable() {
        StringBuilder result = new StringBuilder();
        for (DatedTimetable datedTimetable : timetable) { //зашли в день недели
            result.append('\n')
                    .append(datedTimetable.getDate().toString())
                    .append("\n")
                    .append(datedTimetable.showDatedTimetable());
        }
        return Optional.of(result.toString());
    }
}
