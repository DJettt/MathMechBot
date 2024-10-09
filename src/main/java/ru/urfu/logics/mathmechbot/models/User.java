package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.enums.MathMechBotStateList;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 * @param id внутренний идентификатор пользователя.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @NotNull MathMechBotStateList currentState
) implements Identifiable<Long> {
}
