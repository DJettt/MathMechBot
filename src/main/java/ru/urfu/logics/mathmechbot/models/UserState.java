package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Состояние пользователя (на каком этапе диалога он находится).
 * TODO: отделить этот интерфейс от MathMechBot.
 */
public interface UserState {

    /**
     * Геттер объекта, реализующего логику для данного состояния пользователя.
     *
     * @return объект, реализующий логику состояния.
     */
    @NotNull
    MathMechBotState stateInstance();
}
