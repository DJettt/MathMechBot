package ru.urfu.mathmechbot.jsonparser;

import java.time.LocalDate;
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
    Timetable(LocalDate date) {
        LocalDate mondayDate = findMonday(date);
        for (int i = 0; i < 7; i++) {
            timetable.add(new DatedTimetable(mondayDate.plusDays(i)));
        }
    }

    /**
     * Ищет дату понедельника на неделе с текущей датой.
     * @param date текущая дата
     * @return дату понедельника
     */
    private LocalDate findMonday(LocalDate date) {
        LocalDate currentDate = date;
        while (currentDate.getDayOfWeek().getValue() != 1) {
            currentDate = currentDate.minusDays(1);
        }
        return currentDate;
    }

    /**
     * Добавляет пару в расписание на неделю.
     * @param lesson пара
     * @param date дата пары
     * @param lessonNumber порядковый номер пары
     */
    public void add(Lesson lesson, LocalDate date, Integer lessonNumber) {
        for (DatedTimetable datedTimetable : timetable) {
            if (date.equals(datedTimetable.getDate())) {
                datedTimetable.add(lesson, lessonNumber);
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
