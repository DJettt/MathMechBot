package ru.urfu.mathmechbot.jsonparser;

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
public record Lesson(@NotNull Optional<String> lessonName,
                     @NotNull Optional<String> teacher,
                     @NotNull Optional<String> lessonFormat,
                     @NotNull Optional<String> location,
                     @NotNull Optional<LocalTime> time) {

    /**
     * Собирает строку с информацией о паре.
     * @return строка с информацией
     */
    public String getLessonInfo() {
        StringBuilder stringInfo = new StringBuilder();
        lessonName.ifPresent(s -> stringInfo.append('\t')
                .append(s)
                .append('\n'));
        lessonFormat.ifPresent(s -> stringInfo.append('\t')
                .append(s)
                .append('\n'));
//        teacher.ifPresent(s -> stringInfo.append("\t\t")
//                .append(s)
//                .append('\n'));
        location.ifPresent(s -> stringInfo.append("\t\t")
                .append(s)
                .append('\n'));
        return stringInfo.toString();
    }
}
