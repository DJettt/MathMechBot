package ru.urfu.mathmechbot.timetable;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Достаёт расписание с <a href="https://urfu.ru/ru/students/study/schedule/#/groups">
 * сайта</a>.</p>
 */
public final class TimetableGetter {
    /**
     * <p>Достаёт расписание для переданной группы. Если расписания не нашлось,
     * возвращает пустой Optional.</p>
     *
     * @param men номер группы, чьё расписание ищется.
     * @return Optional с расписанием.
     */
    @NotNull
    public Optional<Timetable> getForGroup(@NotNull String men) {
        // TODO
        final Optional<Timetable> optionalTimetable = getFromCache(men);

        if (optionalTimetable.isPresent()) {
            final Timetable timetable = optionalTimetable.get();
            if (!isTimetableExpired(timetable)) {
                return Optional.of(timetable);
            }
        }

        final Optional<Timetable> newTimetable = getFromApi(men);
        newTimetable.ifPresent(this::putIntoCache);
        return newTimetable;
    }

    /**
     * <p>Ищет расписание на <a href="https://urfu.ru/ru/students/study/schedule/#/groups">
     * *     сайте</a> для данной группы.</p>
     *
     * @param men группа, для которой ищется расписание.
     * @return расписание для данной группы.
     */
    @NotNull
    private Optional<Timetable> getFromApi(@NotNull String men) {
        // TODO
        return Optional.empty();
    }

    /**
     * <p>Проверяет, истёк ли срок актуальности расписания. Расписание должно
     * перестаёт быть актульным, если с момента его создания закончилась одна неделя,
     * то есть прошло воскресенье той недели, на которой расписание было создано. </p>
     *
     * @param timetable расписание.
     * @return результат проверки.
     */
    private boolean isTimetableExpired(@NotNull Timetable timetable) {
        // TODO
        return false;
    }

    /**
     * <p>Достаёт расписание из кэша, если оно там есть.</p>
     *
     * @param men группа, чьё расписание ищется.
     * @return Optional от этого расписания.
     */
    private Optional<Timetable> getFromCache(@NotNull String men) {
        // TODO
        return Optional.empty();
    }

    /**
     * <p>Ложит расписание в кэш.</p>
     *
     * @param timetable расписание.
     */
    private void putIntoCache(@NotNull Timetable timetable) {
        // TODO
    }
}
