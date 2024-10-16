package ru.urfu.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.states.MathMechBotState;

/**
 * Состояние при котором пользователь меняет номер группы.
 */
public final class EditingGroupState extends MathMechBotState {
    private final static Pattern VALID_GROUP_STRING_PATTERN = Pattern.compile("^[1-5]$");
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    Constants.BACK_BUTTON)))
            .build();

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context(), request);
            case null -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            default -> textCommandHandler(context(), request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Возвращаем пользователя на этап выбора поля, которое нужно изменить.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        final LocalMessage message = new EditingChooseState().enterMessage(context, request);
        request.bot().sendMessage(message, request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void textCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        assert request.message().text() != null;

        if (!VALID_GROUP_STRING_PATTERN.matcher(request.message().text()).matches()) {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
            return;
        }

        context.getStorage().getUserEntries().changeUserEntryGroup(request.id(),
                Integer.parseInt(request.message().text()));
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(context, request);
        request.bot().sendMessage(msg, request.id());
    }
}
