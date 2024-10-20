package ru.urfu.logics.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserStorage;

/**
 * Состояние, в котором пользователь уточняет, хочет ли он изменить информацию о себе или нет.
 */
public final class EditingAdditionalEditState implements MathMechBotState {
    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Хотите изменить что-нибудь еще?")
            .buttons(new ArrayList<>(List.of(
                    new Constants().yesButton,
                    new Constants().noButton)))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        final UserStorage userStorage = context.getStorage().getUsers();

        switch (request.message().text()) {
            case Constants.ACCEPT_COMMAND -> {
                userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
                request.bot().sendMessage(new EditingChooseState().enterMessage(context, request), request.id());
            }
            case Constants.DECLINE_COMMAND -> {
                userStorage.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessageBuilder()
                        .text("Изменения успешно сохранены.").build(), request.id());
                new DefaultState().infoCommandHandler(context, request);
                request.bot().sendMessage(new DefaultState().enterMessage(context, request), request.id());
            }
            case null, default -> request.bot().sendMessage(new Constants().tryAgain, request.id());
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return onEnterMessage;
    }
}
