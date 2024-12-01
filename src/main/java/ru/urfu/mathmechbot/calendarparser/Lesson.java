package ru.urfu.mathmechbot.calendarparser;

import java.sql.Time;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Содержит информацию о паре.
 * @param lessonName название пары
 * @param description описание пары (преподаватель)
 * @param date дата пары
 * @param time время начала пары
 * @param location место проведения пары
 */
public record Lesson (@NotNull String lessonName, @Nullable String description, @NotNull Date date, @NotNull Time time,
                      @Nullable String location){

    /**
     * Конструктор на случай если нет информации о преподавателе.
     * @param lessonName название пары
     * @param date дата пары
     * @param time время начала пары
     * @param location место проведения пары
     */
    public Lesson(@NotNull String lessonName, @NotNull Date date, @NotNull Time time,
           @Nullable String location) {
        this(lessonName, "", date, time, location);
    }

    /**
     * Конструктор на случай если нет информации о локации проведения.
     * @param lessonName название пары
     * @param description описание пары (преподаватель)
     * @param date дата пары
     * @param time время начала пары
     */
    public Lesson (@NotNull String lessonName, @Nullable String description, @NotNull Date date, @NotNull Time time){
        this(lessonName, description, date, time, "");
    }

    /**
     * Конструктор на случай если нет информации о локации проведения и преподавателе.
     * @param lessonName название пары
     * @param date дата пары
     * @param time время начала пары
     */
    public Lesson (@NotNull String lessonName, @NotNull Date date, @NotNull Time time){
        this(lessonName, "", date, time, "");
    }
}
