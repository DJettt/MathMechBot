package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.State;
import ru.urfu.logics.mathmechbot.enums.MathMechBotProcess;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 * @param id внутренний идентификатор пользователя.
 * @param telegramId идентификатор пользователя в Telegram.
 * @param discordId идентификатор пользователя в Discord.
 * @param currentProcess многошаговый процесс, в котором находится пользователь.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        @NotNull Long id,
        @Nullable Long telegramId,
        @Nullable Long discordId,
        @Nullable MathMechBotProcess currentProcess,
        @Nullable State currentState
) implements Identifiable<Long> {
}
