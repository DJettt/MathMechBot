package ru.urfu.logics.mathmechbot.models.builders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.State;
import ru.urfu.logics.mathmechbot.enums.MathMechBotProcess;
import ru.urfu.logics.mathmechbot.models.User;

/**
 * Билдер для рекорда User.
 */
public final class UserBuilder {
    private final Long id;
    private Long telegramId;
    private Long discordId;
    private MathMechBotProcess currentProcess;
    private State currentState;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id идентификатор пользователя.
     */
    public UserBuilder(@NotNull Long id) {
        this.id = id;
        this.telegramId = null;
        this.discordId = null;
        this.currentProcess = null;
        this.currentState = null;
    }

    /**
     * Конструктор, копирующий значений из объекта User.
     *
     * @param user объект, у которого скопировать все поля.
     */
    public UserBuilder(@NotNull User user) {
        this.id = user.id();
        this.telegramId = user.telegramId();
        this.discordId = user.discordId();
        this.currentProcess = user.currentProcess();
        this.currentState = user.currentState();
    }

    /**
     * Устанавливает поле telegramId будущего объекта.
     *
     * @param telegramId строка, которую нужно положить в telegramId
     * @return себя же
     */
    public UserBuilder telegramId(@Nullable Long telegramId) {
        this.telegramId = telegramId;
        return this;
    }

    /**
     * Устанавливает поле discordId будущего объекта.
     *
     * @param discordId строка, которую нужно положить в discordId
     * @return себя же
     */
    public UserBuilder discordId(@Nullable Long discordId) {
        this.discordId = discordId;
        return this;
    }

    /**
     * Устанавливает поле currentProcess будущего объекта.
     *
     * @param currentProcess строка, которую нужно положить в discordId
     * @return себя же
     */
    public UserBuilder currentProcess(@Nullable MathMechBotProcess currentProcess) {
        this.currentProcess = currentProcess;
        return this;
    }

    /**
     * Устанавливает поле currentState будущего объекта.
     *
     * @param currentState строка, которую нужно положить в discordId
     * @return себя же
     */
    public UserBuilder currentState(@Nullable State currentState) {
        this.currentState = currentState;
        return this;
    }

    /**
     * Создаёт объект User.
     *
     * @return созданный объект
     */
    public User build() {
        return new User(id, telegramId, discordId, currentProcess, currentState);
    }
}
