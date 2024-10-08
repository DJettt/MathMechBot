package ru.urfu.models;

import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.processes.Process;
import ru.urfu.enums.processes.ProcessState;

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
        @Nullable String telegramId,
        @Nullable String discordId,
        @Nullable Process currentProcess,
        @Nullable ProcessState currentState
) implements Identifiable<Long> {
}
