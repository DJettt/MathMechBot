package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.storages.Identifiable;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 * @param id внутренний идентификатор пользователя.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @NotNull MathMechBotUserState currentState
) implements Identifiable<Long> {
}
