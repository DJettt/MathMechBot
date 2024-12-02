package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Содержит в себе всё расписание на текущий день.
 */
public class DatedTimetable {
    private final LocalDate date;
    private int lastLessonNumber = -1;
    private final List<TimedLesson> datedTimetable = new ArrayList<>();
    private final LocalTime firstTimeMM = LocalTime.of(9,  0, 0);
    private final LocalTime secondTimeMM = LocalTime.of(10,  40, 0);
    private final LocalTime thirdTimeMM = LocalTime.of(12,  50, 0);
    private final LocalTime fourthTimeMM = LocalTime.of(14,  30, 0);
    private final LocalTime fifthTimeMM = LocalTime.of(16,  10, 0);
    private final LocalTime sixthTimeMM = LocalTime.of(17,  50, 0);
    private final LocalTime seventhTimeMM = LocalTime.of(19,  30, 0);
    private final LocalTime firstTimeGuk = LocalTime.of(8,  30, 0);
    private final LocalTime secondTimeGuk = LocalTime.of(10,  15, 0);
    private final LocalTime thirdTimeGuk = LocalTime.of(12,  0, 0);
    private final LocalTime fourthTimeGuk = LocalTime.of(14,  15, 0);
    private final LocalTime fifthTimeGuk = LocalTime.of(16,  0, 0);
    private final LocalTime sixthTimeGuk = LocalTime.of(17,  40, 0);
    private final LocalTime seventhTimeGuk = LocalTime.of(19,  15, 0);

    /**
     * Создает пустое расписание, расписанное по времени, на текущий день.
     * @param date текущая дата
     */
    public DatedTimetable(LocalDate date) {
        this.date = date;
        datedTimetable.add(new TimedLesson(firstTimeMM, firstTimeGuk));
        datedTimetable.add(new TimedLesson(secondTimeMM, secondTimeGuk));
        datedTimetable.add(new TimedLesson(thirdTimeMM, thirdTimeGuk));
        datedTimetable.add(new TimedLesson(fourthTimeMM, fourthTimeGuk));
        datedTimetable.add(new TimedLesson(fifthTimeMM, fifthTimeGuk));
        datedTimetable.add(new TimedLesson(sixthTimeMM, sixthTimeGuk));
        datedTimetable.add(new TimedLesson(seventhTimeMM, seventhTimeGuk));
    }

    /**
     * Устанавливает номер последней пары в текущем дне.
     * @param number номер новой пары
     */
    private void findLatestLessonNumber(int number) {
        if (this.lastLessonNumber < number) {
            this.lastLessonNumber = number;
        }
    }

    /**
     * Добавляет пару в текущее расписание.
     * @param lesson пара
     * @param time время начала пары
     */
    public void add(Lesson lesson, LocalTime time) {
        for (TimedLesson timedLesson : datedTimetable) {
            if (time.equals(timedLesson.getTimeGuk()) || time.equals(timedLesson.getTimeMM())) {
                timedLesson.addLesson(lesson);
                findLatestLessonNumber(datedTimetable.indexOf(timedLesson));
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
        for (int i = 0; i <= lastLessonNumber; i++) {
            result.append(lessonNumber).append(". ");
            if (!datedTimetable.get(i).getAll().isEmpty()) {
                for (Lesson lesson : datedTimetable.get(i).getAll()) {
                    if (lesson.lessonName().isPresent()) {
                        result.append(lesson.lessonName().get())
                                .append('\n');
                    }
                    if (lesson.teacher().isPresent()) {
                        result.append('\t')
                                .append(lesson.teacher().get())
                                .append('\n');
                    }
                    if (lesson.location().isPresent()) {
                        result.append('\t')
                                .append(lesson.location().get())
                                .append('\n');
                    }
                }
            } else {
                result.append("---\n");
            }
            lessonNumber++;
        }
        return result.toString();
    }
}
