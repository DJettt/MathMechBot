package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Содержит в себе пары, которые идут параллельно (например у разных подгрупп одной группы).
 * Используется 2 времени.
 * Одно говорит время текущей пары по расписанию ММ, другое по расписанию ГУКа.
 */
public class TimedLesson {
    private final LocalTime timeMM;
    private final LocalTime timeGuk;
    private final List<Lesson> lessons = new ArrayList<>();

    /**
     * Конструктор.
     * @param timeMM время пары по расписанию Мат-Меха
     * @param timeGuk время пары по расписанию ГУКа
     */
    public TimedLesson(LocalTime timeMM, LocalTime timeGuk) {
        this.timeMM = timeMM;
        this.timeGuk = timeGuk;
    }

    /**
     * Добавляет урок в список параллельно идущих пар.
     * @param lesson урок
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Возвращает время текущей пары по расписанию ММ.
     * @return время текущей пары по расписанию ММ
     */
    public LocalTime getTimeMM() {
        return timeMM;
    }

    /**
     * Возвращает время текущей пары по расписанию ГУКа.
     * @return время текущей пары по расписанию ГУКа
     */
    public LocalTime getTimeGuk() {
        return timeGuk;
    }

    /**
     * Возвращает все параллельно идущие пары у группы.
     * @return все параллельно идущие пары у группы
     */
    public List<Lesson> getAll() {
        return lessons;
    }
}
