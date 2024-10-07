package ru.urfu.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 * @param id внутренний идентификатор пользователя.
 * @param telegramId идентификатор пользователя в Telegram.
 * @param discordId идентификатор пользователя в Discord.
 * @param currentProcess многошаговый процесс, в котором находится пользователь.
 * @param currentState состояние процесса, в котором находится пользователь.
 */
public record User(
        Long id,
        @NotNull String telegramId,
        @NotNull String discordId,
        @Nullable String currentProcess,
        int currentState
) implements Identifiable<Long> {
}
