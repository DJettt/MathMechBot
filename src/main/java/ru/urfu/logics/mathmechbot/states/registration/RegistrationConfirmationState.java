package ru.urfu.logics.mathmechbot.states.registration;

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
 * Состояние подтверждения введённых данных во время регистрации.
 */
public enum RegistrationConfirmationState implements MathMechBotState {
    INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationConfirmationState.class);

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> {
                context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_MEN);
                request.bot().sendMessage(
                        RegistrationMenGroupState.INSTANCE.enterMessage(context, request),
                        request.id());
            }

            case Constants.ACCEPT_COMMAND -> {
                context.storage.users.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessageBuilder().text("Сохранил...").build(), request.id());
                request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
            }

            case Constants.DECLINE_COMMAND -> {
                context.storage.users.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessageBuilder().text("Отмена...").build(), request.id());
                request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
            }

            case null, default -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(request.id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry reached registration end");
            return null;
        }
        final UserEntry userEntry = userEntryOptional.get();

        final String userInfo = Constants.USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        return new LocalMessageBuilder()
                .text(userInfo)
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
    }
}
