package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatedLessons {
    private final LocalDate date;
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

    public DatedLessons(LocalDate date) {
        this.date = date;
        datedTimetable.add(new TimedLesson(firstTimeMM, firstTimeGuk));
        datedTimetable.add(new TimedLesson(secondTimeMM, secondTimeGuk));
        datedTimetable.add(new TimedLesson(thirdTimeMM, thirdTimeGuk));
        datedTimetable.add(new TimedLesson(fourthTimeMM, fourthTimeGuk));
        datedTimetable.add(new TimedLesson(fifthTimeMM, fifthTimeGuk));
        datedTimetable.add(new TimedLesson(sixthTimeMM, sixthTimeGuk));
        datedTimetable.add(new TimedLesson(seventhTimeMM, seventhTimeGuk));
    }

    public LocalDate getDate() {
        return date;
    }

    public void add(Lesson lesson, LocalTime time) {
        for (TimedLesson timedLesson : datedTimetable) {
            if (time.equals(timedLesson.getTimeGuk()) || time.equals(timedLesson.getTimeMM())) {
                timedLesson.addLesson(lesson);
            }
        }
    }

    public List<TimedLesson> getDatedTimetable() {
        return datedTimetable;
    }

    public List<List<Lesson>> getAll() {
        List<List<Lesson>> localTimetable = new ArrayList<>();
        for (TimedLesson timedLesson : datedTimetable) {
            localTimetable.add(timedLesson.getAll());
        }
        return localTimetable;
    }
}
