package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.MMBUserState;

/**
 * <p>Билдер для рекорда User.</p>
 */
public final class UserBuilder {
    private final Long id;
    private final MMBUserState currentState;

    /**
     * <p>Конструктор, устанавливающий дефолтные значения для полей.</p>
     *
     * @param id идентификатор пользователя.
     * @param currentState состояние пользователя.
     */
    public UserBuilder(@NotNull Long id, @NotNull MMBUserState currentState) {
        this.id = id;
        this.currentState = currentState;
    }

    /**
     * <p>Создаёт объект User.</p>
     *
     * @return созданный объект
     */
    public User build() {
        return new User(id, currentState);
    }
}
