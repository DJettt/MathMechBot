package ru.urfu.logics.mathmechbot.states.deletion;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.LocalButton;
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

    private final LocalButton yesButton = new LocalButton("Да", Constants.ACCEPT_COMMAND);
    private final LocalButton noButton = new LocalButton("Нет", Constants.DECLINE_COMMAND);
    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();


    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(contextCore, request);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(contextCore, request);
            case null, default -> {
                request.bot().sendMessage(tryAgain, request.id());
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
                .buttons(List.of(yesButton, noButton, backButton))
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
        request.bot().sendMessage(new LocalMessage("Удаляем..."), request.id());
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
        request.bot().sendMessage(new LocalMessage("Отмена..."), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }
}
