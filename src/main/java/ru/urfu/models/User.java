package ru.urfu.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Модель пользователя, подписавшегося на пересылку информации.
 */
public final class User implements Identifiable<Long> {
    private final Long id;
    @NotNull
    private final Long telegramId;
    @NotNull
    private final String discordId;
    @Nullable
    private String currentProcess;
    private int currentState;

    /**
     * @param id             внутренний идентификатор пользователя.
     * @param telegramId     идентификатор пользователя в Telegram.
     * @param discordId      идентификатор пользователя в Discord.
     * @param currentProcess многошаговый процесс, в котором находится пользователь.
     * @param currentState   состояние процесса, в котором находится пользователь.
     */
    public User(
            Long id,
            @NotNull Long telegramId,
            @NotNull String discordId,
            @Nullable String currentProcess,
            int currentState
    ) {
        this.id = id;
        this.telegramId = telegramId;
        this.discordId = discordId;
        this.currentProcess = currentProcess;
        this.currentState = currentState;
    }

    public void setCurrentProcess(String process) {
        this.currentProcess = process;
    }

    public String getCurrentProcess() {
        return this.currentProcess;
    }

    public void setCurrentState(int position){
        this.currentState = position;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    @Override
    public Long id() {
        return id;
    }

    @NotNull
    public Long telegramId() {
        return telegramId;
    }

    @NotNull
    public String discordId() {
        return discordId;
    }

    @Nullable
    public String currentProcess() {
        return currentProcess;
    }

    public int currentState(int i) {
        return currentState;
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
                this.currentState == that.currentState;
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
