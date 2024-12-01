package ru.urfu.mathmechbot.timetable;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Фабрика расписаний.</p>
 */
public interface TimetableFactory {
    /**
     * <p>Достаёт расписание для переданной группы. Если расписания не нашлось,
     * возвращает пустой Optional.</p>
     *
     * @param men номер группы, чьё расписание ищется.
     * @return Optional с расписанием.
     */
    @NotNull
    Optional<DailyTimetable> getForGroup(@NotNull String men);
}
