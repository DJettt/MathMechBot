package ru.urfu.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.states.DefaultState;
import ru.urfu.mathmechbot.states.MathMechBotState;

/**
 * Состояние, в котором пользователь уточняет, хочет ли он изменить информацию о себе или нет.
 */
public final class EditingAdditionalEditState extends MathMechBotState {
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("Хотите изменить что-нибудь еще?")
            .buttons(new ArrayList<>(List.of(
                    Constants.YES_BUTTON,
                    Constants.NO_BUTTON)))
            .build();

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        switch (request.message().text()) {
            case Constants.ACCEPT_COMMAND -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_CHOOSE);
                request.bot().sendMessage(new EditingChooseState().enterMessage(context(), request), request.id());
            }
            case Constants.DECLINE_COMMAND -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.DEFAULT);
                request.bot().sendMessage(new LocalMessageBuilder()
                        .text("Изменения успешно сохранены.").build(), request.id());
                new DefaultState().infoCommandHandler(context(), request);
                request.bot().sendMessage(new DefaultState().enterMessage(context(), request), request.id());
            }
            case null, default -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        return ON_ENTER_MESSAGE;
    }
}
