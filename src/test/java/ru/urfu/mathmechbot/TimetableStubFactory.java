package ru.urfu.mathmechbot;

import java.time.LocalDate;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.timetable.DailyTimetable;
import ru.urfu.mathmechbot.timetable.TimetableFactory;

/**
 * <p>Фабрика, возвращающая заранее предопределённые расписания для конкретных групп.</p>
 */
public final class TimetableStubFactory implements TimetableFactory {
    /**
     * <p>Стандартное расписание с окнами.</p>
     */
    private final DailyTimetable standard =
            new DailyTimetable(LocalDate.now());

    /**
     * <p>Расписание с парами, у которых не указано время.</p>
     */
    private final DailyTimetable withLessonsWithoutTime =
            new DailyTimetable(LocalDate.now());

    /**
     * <p>Расписание только с парами, у которых не указано время.</p>
     */
    private final DailyTimetable onlyLessonsWithoutTime =
            new DailyTimetable(LocalDate.now());

    /**
     * <p>Конструктор.</p>
     */
    public TimetableStubFactory() {
        // TODO: заполнить DailyTimetable нужным содержимым.
    }

    @Override
    public @NotNull Optional<DailyTimetable> getForGroup(@NotNull String men,
                                                         @NotNull LocalDate date) {
        return switch (men) {
            case "МЕН-111111" -> Optional.of(standard);
            case "МЕН-222222" -> Optional.of(withLessonsWithoutTime);
            case "МЕН-333333" -> Optional.of(onlyLessonsWithoutTime);
            default -> Optional.empty();
        };
    }
}
