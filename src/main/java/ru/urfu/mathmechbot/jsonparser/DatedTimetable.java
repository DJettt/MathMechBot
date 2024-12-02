package ru.urfu.mathmechbot.jsonparser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Содержит в себе всё расписание на текущий день.
 */
public class DatedTimetable {
    private final LocalDate date;
    private int lastLessonNumber = -1;
    private final List<Lesson> untimedLessons = new ArrayList<>();
    private final static int DEFAULT_LESSON_NUMBER = 0;
    private final List<TimedLesson> datedTimetable = new ArrayList<>();
    private final static int FIRST_NUMBER = 1;
    private final static int SECOND_NUMBER = 2;
    private final static int THIRD_NUMBER = 3;
    private final static int FOURTH_NUMBER = 4;
    private final static int FIFTH_NUMBER = 5;
    private final static int SIXTH_NUMBER = 6;
    private final static int SEVENTH_NUMBER = 7;

    /**
     * Создает пустое расписание на текущий день.
     * @param date текущая дата
     */
    public DatedTimetable(LocalDate date) {
        this.date = date;
        datedTimetable.add(new TimedLesson(FIRST_NUMBER));
        datedTimetable.add(new TimedLesson(SECOND_NUMBER));
        datedTimetable.add(new TimedLesson(THIRD_NUMBER));
        datedTimetable.add(new TimedLesson(FOURTH_NUMBER));
        datedTimetable.add(new TimedLesson(FIFTH_NUMBER));
        datedTimetable.add(new TimedLesson(SIXTH_NUMBER));
        datedTimetable.add(new TimedLesson(SEVENTH_NUMBER));
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
            untimedLessons.add(lesson);
            return;
        }
        for (TimedLesson timedLesson : datedTimetable) {
            if (lessonNumber == timedLesson.getLessonNumber()) {
                timedLesson.addLesson(lesson);
                findLatestLessonNumber(lessonNumber);
            }
        }
    }

    /**
     * Возвращает текущую дату.
     * @return текущая дата
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Возвращает номер последней пары на текущий день.
     * @return номер последней пары.
     */
    public int getlastLessonNumberNumber() {
        return lastLessonNumber;
    }

    /**
     * Возвращает расписание на текущий день.
     * @return расписание на день
     */
    public List<TimedLesson> getDatedTimetable() {
        return datedTimetable;
    }

    /**
     * Возвращает все пары в виде списка списков.
     * @return все пары
     */
    public List<List<Lesson>> getAll() {
        List<List<Lesson>> localTimetable = new ArrayList<>();
        for (int i = 0; i < lastLessonNumber; i++) {
            localTimetable.add(datedTimetable.get(i).getAll());
        }
        return localTimetable;
    }

    /**
     * Собирает строку с расписанием.
     * @return расписание в виде строки
     */
    public String showDatedTimetable() {
        StringBuilder result = new StringBuilder();
        int lessonNumber = 1;
        for (int i = 0; i < lastLessonNumber; i++) {
            result.append(lessonNumber).append(".");
            if (!datedTimetable.get(i).getAll().isEmpty()) {
                for (Lesson lesson : datedTimetable.get(i).getAll()) {
                    result.append(lesson.getLessonInfo());
                }
            } else {
                result.append("\t---\n");
            }
            lessonNumber++;
        }
        if (!untimedLessons.isEmpty()) {
            result.append("\nЗанятия без точного времени:\n");
            for (Lesson lesson : untimedLessons) {
                result.append(lesson.getLessonInfo());
            }
        }
        return result.toString();
    }
}
