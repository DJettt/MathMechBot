package ru.urfu.mathmechbot.timetable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;


/**
 * Содержит в себе всё расписание на текущий день.
 */
public final class DailyTimetable {
    private final LocalDate date;
    private int lastLessonNumber = -1;
    private final List<Lesson> lessonsWithoutNumber = new ArrayList<>();
    private final static int DEFAULT_LESSON_NUMBER = 0;
    private final Map<Integer, List<Lesson>> dailyTimetable = new HashMap<>(7, 1f);

    /**
     * Создает пустое расписание на текущий день.
     * @param date текущая дата
     */
    public DailyTimetable(@NotNull LocalDate date) {
        this.date = date;
    }

    /**
     * Устанавливает номер последней пары в текущем дне.
     * @param number порядковый номер новой пары
     */
    private void findLatestLessonNumber(int number) {
        if (this.lastLessonNumber < number) {
            this.lastLessonNumber = number;
        }
    }

    /**
     * Добавляет пару в текущее расписание.
     * @param lesson пара
     * @param lessonNumber порядковый номер пары
     */
    public void add(Lesson lesson, int lessonNumber) {
        if (lessonNumber == DEFAULT_LESSON_NUMBER) {
            lessonsWithoutNumber.add(lesson);
            return;
        }
        dailyTimetable.putIfAbsent(lessonNumber, new ArrayList<>());
        dailyTimetable.get(lessonNumber).add(lesson);
        findLatestLessonNumber(lessonNumber);
    }

    /**
     * Возвращает текущую дату.
     * @return текущая дата
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Собирает строку с расписанием.
     * @return расписание в виде строки
     */
    public String showDailyTimetable() {
        StringBuilder result = new StringBuilder();
        for (Integer lessonNumber = 1; lessonNumber <= lastLessonNumber; lessonNumber++) {
            result.append(lessonNumber).append(".");
            if (dailyTimetable.containsKey(lessonNumber)) {
                for (Lesson lesson : dailyTimetable.get(lessonNumber)) {
                    result.append(lesson.getLessonInfo());
                }
            } else {
                result.append("   ---\n\n");
            }
        }
        if (!lessonsWithoutNumber.isEmpty()) {
            result.append("\nЗанятия без точного времени:\n");
            for (Lesson lesson : lessonsWithoutNumber) {
                result.append(lesson.getLessonInfo());
            }
        }
        return result.toString();
    }
}
