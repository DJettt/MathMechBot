package ru.urfu.logics.mathmechbot.models.builders;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.enums.MathMechBotStateList;
import ru.urfu.logics.mathmechbot.models.User;

/**
 * Билдер для рекорда User.
 */
public final class UserBuilder {
    private final Long id;
    private final MathMechBotStateList currentState;

    /**
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id идентификатор пользователя.
     * @param currentState состояние пользователя.
     */
    public UserBuilder(@NotNull Long id, @NotNull MathMechBotStateList currentState) {
        this.id = id;
        this.currentState = currentState;
    }

    /**
     * Конструктор, копирующий значений из объекта User.
     *
     * @param user объект, у которого скопировать все поля.
     */
    public UserBuilder(@NotNull User user) {
        this.id = user.id();
        this.currentState = user.currentState();
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
