package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.UserState;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.states.MathMechBotState;

/**
 * Билдер для рекорда User.
 */
public final class UserBuilder {
    private final Long id;
    private final UserState<MathMechBotCore, MathMechBotState> currentState;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id идентификатор пользователя.
     * @param currentState состояние пользователя.
     */
    public UserBuilder(@NotNull Long id, @NotNull UserState<MathMechBotCore, MathMechBotState> currentState) {
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
