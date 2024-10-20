package ru.urfu.logics.mathmechbot.states.deletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public enum DeletionConfirmationState implements MathMechBotState {
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger(DeletionConfirmationState.class);

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(context, request);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(context, request);
            case null, default -> {
                request.bot().sendMessage(new Constants().tryAgain, request.id());
                request.bot().sendMessage(enterMessage(context, request), request.id());
            }
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry reached deletion confirmation state");
            return null;
        }

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userEntryOptional.get().toHumanReadable())
                .buttons(new ArrayList<>(List.of(
                        new Constants().yesButton,
                        new Constants().noButton,
                        new Constants().backButton)))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Обрабатывает команду согласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void acceptCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        userEntryOptional.ifPresent(context.getStorage().getUserEntries()::delete);

        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), request.id());
        request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Обрабатывает команду несогласия: переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void declineCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Отмена...").build(), request.id());
        request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
    }
}
