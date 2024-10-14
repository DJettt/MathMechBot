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

/**
 * Состояние, в котором пользователь уточняет, хочет ли он изменить информацию о себе или нет.
 */
public enum EditingAdditionalEditState implements MathMechBotState {
    INSTANCE;

    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("Хотите изменить что-нибудь еще?")
            .buttons(new ArrayList<>(List.of(
                    Constants.YES_BUTTON,
                    Constants.NO_BUTTON)))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.ACCEPT_COMMAND -> {
                context.storage.users.changeUserState(request.id(),
                        MathMechBotUserState.EDITING_CHOOSE);
                request.bot().sendMessage(EditingChooseState.INSTANCE.enterMessage(context, request), request.id());
            }
            case Constants.DECLINE_COMMAND -> {
                context.storage.users.changeUserState(request.id(),
                        MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessageBuilder()
                        .text("Изменения успешно сохранены.").build(), request.id());
                request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
            }
            default -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
        }
    }

    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return ON_ENTER_MESSAGE;
    }
}
