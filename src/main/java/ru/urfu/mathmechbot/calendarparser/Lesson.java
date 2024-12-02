package ru.urfu.mathmechbot.calendarparser;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Содержит информацию о паре.
 * @param lessonName название пары
 * @param teacher описание пары (преподаватель)
 * @param location место проведения пары
 */
public record Lesson(@NotNull Optional<String> lessonName,
                      @NotNull Optional<String> teacher,
                      @NotNull Optional<String> location){
}
