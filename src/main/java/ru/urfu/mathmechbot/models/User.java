package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.MMBUserState;
import ru.urfu.storages.Identifiable;

/**
 * <p>Модель пользователя, подписавшегося на пересылку информации.</p>
 *
 * @param id внутренний идентификатор пользователя.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @NotNull MMBUserState currentState
) implements Identifiable<Long> {
}
