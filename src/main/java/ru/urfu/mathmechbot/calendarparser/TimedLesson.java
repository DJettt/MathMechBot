package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimedLesson {
    private final LocalTime timeMM;
    private final LocalTime timeGuk;
    private final List<Lesson> lessons = new ArrayList<>();

    public TimedLesson(LocalTime timeMM, LocalTime timeGuk) {
        this.timeMM = timeMM;
        this.timeGuk = timeGuk;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public LocalTime getTimeMM() {
        return timeMM;
    }

    public LocalTime getTimeGuk() {
        return timeGuk;
    }

    public List<Lesson> getAll() {
        return lessons;
    }
}
