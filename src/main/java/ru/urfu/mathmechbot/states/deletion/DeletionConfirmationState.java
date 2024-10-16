package ru.urfu.mathmechbot.states.deletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.states.DefaultState;
import ru.urfu.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public final class DeletionConfirmationState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeletionConfirmationState.class);

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context(), request);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(context(), request);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(context(), request);
            case null, default -> {
                request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
                request.bot().sendMessage(enterMessage(context(), request), request.id());
            }
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry reached deletion confirmation state");
            return null;
        }

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userEntryOptional.get().toHumanReadable())
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new DefaultState().enterMessage(context, request), request.id());
    }

    /**
     * Обрабатывает команду согласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void acceptCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        userEntryOptional.ifPresent(context.getStorage().getUserEntries()::delete);

        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(context, request), request.id());
    }

    /**
     * Обрабатывает команду несогласия: переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void declineCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Отмена...").build(), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(context, request), request.id());
    }
}
