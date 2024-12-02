package ru.urfu.mathmechbot.timetable;

import java.time.LocalTime;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Содержит информацию о паре.
 * @param lessonName название пары
 * @param teacher преподаватель
 * @param lessonFormat формат пары (лекции/практические занятия)
 * @param location место проведения пары
 * @param time время начала пары
 */
public record Lesson(@NotNull String lessonName,
                     @NotNull Optional<String> teacher,
                     @NotNull Optional<String> lessonFormat,
                     @NotNull Optional<String> location,
                     @NotNull Optional<LocalTime> time) {
    private final static String BIG_SPACE = "         ";
    private final static String SMALL_SPACE = "   ";

    /**
     * Собирает строку с информацией о паре.
     * @return строка с информацией
     */
    @NotNull
    public String getLessonInfo() {
        StringBuilder stringInfo = new StringBuilder();
        stringInfo.append(SMALL_SPACE)
                .append(lessonName)
                .append('\n');
        lessonFormat.ifPresent(s -> stringInfo.append(BIG_SPACE)
                .append(s)
                .append('\n'));
//        teacher.ifPresent(s -> stringInfo.append("\t\t")
//                .append(s)
//                .append('\n'));
        location.ifPresent(s -> stringInfo.append(BIG_SPACE)
                .append(s)
                .append("\n\n"));
        return stringInfo.toString();
    }
}
