package ru.urfu.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.processes.Process;
import ru.urfu.enums.processes.ProcessState;

import java.util.Objects;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 */
public final class User implements Identifiable<Long> {
    @NotNull
    private final Long id;
    @Nullable
    private final Long telegramId;
    @Nullable
    private final Long discordId;
    @Nullable
    private Process currentProcess;
    @Nullable
    private ProcessState currentState;

    /**
     * @param id             внутренний идентификатор пользователя.
     * @param telegramId     идентификатор пользователя в Telegram.
     * @param discordId      идентификатор пользователя в Discord.
     * @param currentProcess многошаговый процесс, в котором находится пользователь.
     * @param currentState   состояние процесса, в котором находится пользователь.
     */
    public User(
            @NotNull Long id,
            @Nullable Long telegramId,
            @Nullable Long discordId,
            @Nullable Process currentProcess,
            @Nullable ProcessState currentState
    ) {
        this.id = id;
        this.telegramId = telegramId;
        this.discordId = discordId;
        this.currentProcess = currentProcess;
        this.currentState = currentState;
    }

    @Override
    @NotNull
    public Long id() {
        return id;
    }

    @Nullable
    public Long telegramId() {
        return telegramId;
    }

    @Nullable
    public Long discordId() {
        return discordId;
    }

    @Nullable
    public Process currentProcess() {
        return currentProcess;
    }

    public Process getCurrentProcess(){
        return currentProcess;
    }
    public void setCurrentProcess(@Nullable Process process) {
        this.currentProcess = process;
    }

    @Nullable
    public ProcessState currentState() {
        return currentState;
    }

    public void setCurrentState(@Nullable ProcessState state) {
        this.currentState = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.telegramId, that.telegramId) &&
                Objects.equals(this.discordId, that.discordId) &&
                Objects.equals(this.currentProcess, that.currentProcess) &&
                Objects.equals(this.currentState, that.currentState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramId, discordId, currentProcess, currentState);
    }

    @Override
    public String toString() {
        return "User[" +
                "id=" + id + ", " +
                "telegramId=" + telegramId + ", " +
                "discordId=" + discordId + ", " +
                "currentProcess=" + currentProcess + ", " +
                "currentState=" + currentState + ']';
    }

}
