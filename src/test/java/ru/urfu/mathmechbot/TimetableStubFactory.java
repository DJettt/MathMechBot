package ru.urfu.mathmechbot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.timetable.DailyTimetable;
import ru.urfu.mathmechbot.timetable.Lesson;
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
     * <p>Расписание только с парами, у которых не указан порядковый номер.</p>
     */
    private final DailyTimetable onlyLessonsWithoutNumber =
            new DailyTimetable(LocalDate.now());

    /**
     * <p>Конструктор.</p>
     */
    @SuppressWarnings("MagicNumber")
    public TimetableStubFactory() {
        Lesson testLesson1 = new Lesson("Английский язык",
                Optional.of("Иванов"),
                Optional.of("практика"),
                Optional.of("1 Пушкина 1"),
                Optional.of(LocalTime.of(9, 0, 0)));
        Lesson testLesson2 = new Lesson("Питон",
                Optional.empty(),
                Optional.of("совмещенные занятия"),
                Optional.of("Мира 1"),
                Optional.of(LocalTime.of(11, 50, 0)));
        Lesson testLesson3 = new Lesson("Физкультура",
                Optional.of("Петров"),
                Optional.empty(),
                Optional.of("стадион"),
                Optional.of(LocalTime.of(10, 40, 0)));
        Lesson testLesson4 = new Lesson("Философия",
                Optional.of("Евклидов"),
                Optional.of("рассуждение"),
                Optional.of("под звездным небом"),
                Optional.empty());
        Lesson testLesson5 = new Lesson("Матанализ",
                Optional.of("Соколов"),
                Optional.of("лекция"),
                Optional.empty(),
                Optional.empty());
        Lesson testLesson6 = new Lesson("История",
                Optional.of("Ульянов"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        Lesson testLesson7 = new Lesson("ООП",
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
        Lesson testLesson8 = new Lesson("АрхЭВМ",
                Optional.empty(),
                Optional.of("разборка процессоров"),
                Optional.empty(),
                Optional.empty());
        standard.add(testLesson1, 1);
        standard.add(testLesson2, 3);
        standard.add(testLesson3, 3);
        standard.add(testLesson4, 3);
        standard.add(testLesson5, 4);
        standard.add(testLesson6, 4);
        standard.add(testLesson7, 6);
        standard.add(testLesson8, 6);
        withLessonsWithoutTime.add(testLesson1, 0);
        withLessonsWithoutTime.add(testLesson2, 0);
        withLessonsWithoutTime.add(testLesson3, 1);
        withLessonsWithoutTime.add(testLesson4, 3);
        withLessonsWithoutTime.add(testLesson5, 3);
        withLessonsWithoutTime.add(testLesson6, 5);
        withLessonsWithoutTime.add(testLesson7, 5);
        withLessonsWithoutTime.add(testLesson8, 0);
        onlyLessonsWithoutNumber.add(testLesson1, 0);
        onlyLessonsWithoutNumber.add(testLesson2, 0);
        onlyLessonsWithoutNumber.add(testLesson3, 0);
        onlyLessonsWithoutNumber.add(testLesson4, 0);
        onlyLessonsWithoutNumber.add(testLesson5, 0);
        onlyLessonsWithoutNumber.add(testLesson6, 0);
        onlyLessonsWithoutNumber.add(testLesson7, 0);
        onlyLessonsWithoutNumber.add(testLesson8, 0);
    }

    @Override
    public @NotNull Optional<DailyTimetable> getForGroup(@NotNull String men,
                                                         @NotNull LocalDate date) {
        return switch (men) {
            case "МЕН-111111" -> Optional.of(standard);
            case "МЕН-222222" -> Optional.of(withLessonsWithoutTime);
            case "МЕН-333333" -> Optional.of(onlyLessonsWithoutNumber);
            default -> Optional.empty();
        };
    }
}
