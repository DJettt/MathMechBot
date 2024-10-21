package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;

/**
 * Состояние при котором пользователь меняет номер группы.
 */
public final class EditingGroupState implements MathMechBotState {
    private final Pattern validGroupStringPattern = Pattern.compile("^[1-5]$");
    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    new Constants().backButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> request.bot().sendMessage(new Constants().tryAgain, request.id());
            default -> textCommandHandler(contextCore, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Возвращаем пользователя на этап выбора поля, которое нужно изменить.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        final LocalMessage message = new EditingChooseState().enterMessage(contextCore, request);
        request.bot().sendMessage(message, request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void textCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        assert request.message().text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        if (!validGroupStringPattern.matcher(request.message().text()).matches()) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
            return;
        }

        userEntryStorage.changeUserEntryGroup(request.id(), Integer.parseInt(request.message().text()));
        userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, request);
        request.bot().sendMessage(msg, request.id());
    }
}
