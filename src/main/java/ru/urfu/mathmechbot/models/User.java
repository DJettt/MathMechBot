package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.storages.Identifiable;

/**
 * <p>Модель пользователя, подписавшегося на пересылку информации.</p>
 *
 * @param id внутренний идентификатор пользователя.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @NotNull UserState currentState
) implements Identifiable<Long> {

    @Override
    public Long getId() {
        return this.id;
    }
}
