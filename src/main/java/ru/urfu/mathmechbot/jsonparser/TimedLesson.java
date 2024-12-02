package ru.urfu.mathmechbot.jsonparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Содержит в себе пары, которые идут параллельно (например у разных подгрупп одной группы).
 * Поле lessonNumber указывает на порядковый номер пары.
 */
public class TimedLesson {
    private final int lessonNumber;
    private final List<Lesson> lessons = new ArrayList<>();

    /**
     * Конструктор.
     * @param number порядковый номер пары
     */
    public TimedLesson(int number) {
        this.lessonNumber = number;
    }

    /**
     * Добавляет урок в список параллельно идущих пар.
     * @param lesson урок
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Возвращает порядковый номер пары.
     * @return порядковый номер пары
     */
    public int getLessonNumber() {
        return lessonNumber;
    }

    /**
     * Возвращает все параллельно идущие пары у группы.
     * @return все параллельно идущие пары у группы
     */
    public List<Lesson> getAll() {
        return lessons;
    }
}
