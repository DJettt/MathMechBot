package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalButton;
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
    private final LocalButton yesButton = new LocalButton("Да", Constants.ACCEPT_COMMAND);
    private final LocalButton noButton = new LocalButton("Нет", Constants.DECLINE_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Хотите изменить что-нибудь еще?")
            .buttons(List.of(yesButton, noButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();

        switch (request.message().text()) {
            case Constants.ACCEPT_COMMAND -> {
                userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
                request.bot().sendMessage(new EditingChooseState().enterMessage(contextCore, request), request.id());
            }
            case Constants.DECLINE_COMMAND -> {
                userStorage.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessage("Изменения успешно сохранены."), request.id());
                new DefaultState().infoCommandHandler(contextCore, request);
                request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
            }
            case null, default -> request.bot().sendMessage(tryAgain, request.id());
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return onEnterMessage;
    }
}
