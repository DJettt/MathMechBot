package ru.urfu.mathmechbot.fsm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.fsm.EventHandler;
import ru.urfu.fsm.RequestEvent;
import ru.urfu.mathmechbot.models.MathMechBotUserState;

/**
 * Переход между состояниями.
 *
 * @param name         имя перехода.
 * @param sourceState  входное состояние.
 * @param targetState  выходное состояние.
 * @param eventType    класс событий, которые триггирят данный переход.
 * @param eventHandler обработчика этих событий.
 */
public record Transition(
        @NotNull String name,
        @NotNull MathMechBotUserState sourceState,
        @NotNull MathMechBotUserState targetState,
        @NotNull Class<? extends RequestEvent> eventType,
        @Nullable EventHandler eventHandler
) {
}
