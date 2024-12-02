package ru.urfu.mathmechbot.timetable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Обёртка для {@link TimetableApiFactory TimetableApiFactory}, которая добавляет прослойку
 * с кэшем и проверяет актуальность расписания.</p>
 */
public final class TimetableCachedFactory implements TimetableFactory {
    private final ConcurrentMap<String, DailyTimetable> cache = new ConcurrentHashMap<>();
    private final TimetableFactory timetableFactory = new TimetableApiFactory();
    private final TimetableUtils utils = new TimetableUtils();

    @Override
    @NotNull
    public Optional<DailyTimetable> getForGroup(@NotNull String men) {
        final DailyTimetable timetable = cache.get(men);
        if (timetable != null && !isTimetableExpired(timetable)) {
            return Optional.of(timetable);
        }

        final Optional<DailyTimetable> newTimetable = timetableFactory.getForGroup(men);
        newTimetable.ifPresent(t -> cache.put(men, t));
        return newTimetable;
    }

    /**
     * <p>Проверяет, истёк ли срок актуальности расписания. Расписание должно
     * перестаёт быть актуальным, если с момента его создания закончилась одна неделя,
     * то есть прошло воскресенье той недели, на которой расписание было создано.</p>
     *
     * @param timetable расписание.
     * @return результат проверки.
     */
    private boolean isTimetableExpired(@NotNull DailyTimetable timetable) {
        final int timetableWeek = utils.getWeekNumber(timetable.date());
        final int currentWeek = utils.getWeekNumber(LocalDate.now());
        return timetableWeek != currentWeek;
    }
}
