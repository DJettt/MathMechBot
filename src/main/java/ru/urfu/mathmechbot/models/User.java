package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.UserState;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.states.MathMechBotState;
import ru.urfu.storages.Identifiable;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 * @param id внутренний идентификатор пользователя.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @NotNull UserState<MathMechBotCore, MathMechBotState> currentState
) implements Identifiable<Long> {
}
