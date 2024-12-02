package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timetable {
    private final List<DatedLessons> timetable = new ArrayList<>();

    Timetable(LocalDate date) {
        timetable.add(new DatedLessons(date));
        timetable.add(new DatedLessons(date.plusDays(1)));
        timetable.add(new DatedLessons(date.plusDays(2)));
        timetable.add(new DatedLessons(date.plusDays(3)));
        timetable.add(new DatedLessons(date.plusDays(4)));
        timetable.add(new DatedLessons(date.plusDays(5)));
        timetable.add(new DatedLessons(date.plusDays(6)));
        timetable.add(new DatedLessons(date.plusDays(7)));
    }

    public void add(Lesson lesson, LocalDate date, LocalTime time) {
        for (DatedLessons datedLessons : timetable) {
            if (date.equals(datedLessons.getDate())) {
                datedLessons.add(lesson, time);
                return;
            }
        }
    }

    public List<List<Lesson>> getByDate(LocalDate date) {
        for (DatedLessons datedLessons : timetable) {
            if (date.equals(datedLessons.getDate())) {
                return datedLessons.getAll();
            }
        }
        return Collections.emptyList();
    }

    public String showByDate(LocalDate date) {
        StringBuilder result = new StringBuilder();
        for (DatedLessons datedLessons : timetable) { //зашли в день недели
            if (date.equals(datedLessons.getDate())) {
                result.append(datedLessons.getDate().toString()).append("\n");
                int i = 1;
                for (TimedLesson timedLesson : datedLessons.getDatedTimetable()) { // зашли в пары
                    result.append(i).append(". ");
                    for (Lesson lesson : timedLesson.getAll()) { //зашли в лист одновременных пар
                        if (lesson.lessonName().isPresent()) {
                            result.append('\t')
                                    .append(lesson.lessonName().get())
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
                    result.append('\n');
                    i++;
                }
            }
        }
        return result.toString();
    }

    public String showTimetable() {
        StringBuilder result = new StringBuilder();
        for (DatedLessons datedLessons : timetable) { //зашли в день недели
            result.append(datedLessons.getDate().toString()).append("\n");
            int i = 1;
            for (TimedLesson timedLesson : datedLessons.getDatedTimetable()) { // зашли в пары
                result.append(i).append(". ");
                for (Lesson lesson : timedLesson.getAll()) { //зашли в лист одновременных пар
                    if (lesson.lessonName().isPresent()) {
                        result.append('\t')
                                .append(lesson.lessonName().get())
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
                result.append('\n');
                i++;
            }
        }
        return result.toString();
    }
}
