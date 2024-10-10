package ru.urfu.logics.mathmechbot.models.builders;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.userstates.MathMechBotUserState;

/**
 * Билдер для рекорда User.
 */
public final class UserBuilder {
    private final Long id;
    private final MathMechBotUserState currentState;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id идентификатор пользователя.
     * @param currentState состояние пользователя.
     */
    public UserBuilder(@NotNull Long id, @NotNull MathMechBotUserState currentState) {
        this.id = id;
        this.currentState = currentState;
    }

    /**
     * Создаёт объект User.
     *
     * @return созданный объект
     */
    public User build() {
        return new User(id, currentState);
    }
}
