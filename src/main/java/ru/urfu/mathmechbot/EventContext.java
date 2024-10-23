package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;

public record EventContext(
        @NotNull User user,
        @NotNull LocalMessage message,
        @NotNull Bot bot
) {
}
