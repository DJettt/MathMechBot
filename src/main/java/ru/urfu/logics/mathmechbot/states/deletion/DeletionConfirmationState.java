package ru.urfu.logics.mathmechbot.states.deletion;

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
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public final class DeletionConfirmationState implements MathMechBotState {
    private final Logger logger = LoggerFactory.getLogger(DeletionConfirmationState.class);

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(contextCore, request);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(contextCore, request);
            case null, default -> {
                request.bot().sendMessage(new Constants().tryAgain, request.id());
                request.bot().sendMessage(enterMessage(contextCore, request), request.id());
            }
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(request.id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry reached deletion confirmation state");
            return null;
        }

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userEntryOptional.get().toHumanReadable())
                .buttons(List.of(
                        new Constants().yesButton,
                        new Constants().noButton,
                        new Constants().backButton))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Обрабатывает команду согласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void acceptCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final Optional<UserEntry> userEntryOptional = userEntryStorage.get(request.id());
        userEntryOptional.ifPresent(userEntryStorage::delete);

        userStorage.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Обрабатывает команду несогласия: переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void declineCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Отмена...").build(), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }
}
