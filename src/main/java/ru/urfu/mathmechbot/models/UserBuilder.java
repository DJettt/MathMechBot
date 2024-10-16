package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.MMBUserState;

/**
 * Билдер для рекорда User.
 */
public final class UserBuilder {
    private final Long id;
    private final MMBUserState currentState;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id идентификатор пользователя.
     * @param currentState состояние пользователя.
     */
    public UserBuilder(@NotNull Long id, @NotNull MMBUserState currentState) {
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
