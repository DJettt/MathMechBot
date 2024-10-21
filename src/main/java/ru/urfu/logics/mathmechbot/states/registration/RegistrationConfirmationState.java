package ru.urfu.logics.mathmechbot.states.registration;

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
 * Состояние подтверждения введённых данных во время регистрации.
 */
public final class RegistrationConfirmationState implements MathMechBotState {
    private final static String ENTER_MESSAGE_PREFIX = "Всё верно?\n\n";

    private final Logger logger = LoggerFactory.getLogger(RegistrationConfirmationState.class);

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(contextCore, request);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(contextCore, request);
            case null, default -> request.bot().sendMessage(new Constants().tryAgain, request.id());
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(request.id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry reached registration end");
            return null;
        }

        return new LocalMessageBuilder()
                .text(ENTER_MESSAGE_PREFIX + userEntryOptional.get().toHumanReadable())
                .buttons(List.of(
                        new Constants().yesButton,
                        new Constants().noButton,
                        new Constants().backButton))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в состояние запрос МЕН-группы.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_MEN);
        request.bot().sendMessage(
                new RegistrationMenGroupState().enterMessage(contextCore, request),
                request.id());
    }

    /**
     * Обрабатывает команду согласия: сохраняет данные пользователя, переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void acceptCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessage("Сохранил..."), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Обрабатывает команду несогласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void declineCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(request.id());
        userEntryOptional.ifPresent(contextCore.getStorage().getUserEntries()::delete);
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new LocalMessage("Отмена..."), request.id());
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }
}
