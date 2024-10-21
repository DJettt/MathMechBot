package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
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
 * Состояние изменения академической группы (МЕН-123456).
 */
public final class EditingMenState implements MathMechBotState {
    private final Pattern validMenGroupString = Pattern.compile("^МЕН-\\d{6}$");

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> request.bot().sendMessage(new Constants().tryAgain, request.id());
            default -> textHandler(contextCore, request);
        }
    }

    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(List.of(new Constants().backButton))
                .build();
    }

    /**
     * Возвращает пользователя на шаг назад, то есть на этап выбора, что конкретно изменить.
     *
     * @param contextCore контекст состояния.
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(new EditingChooseState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Обработчик текстовых сообщений.
     *
     * @param contextCore контекст состояния.
     * @param request запрос.
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        assert request.message().text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final String trimmedText = request.message().text().trim();
        if (!validMenGroupString.matcher(trimmedText).matches()) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            return;
        }

        userEntryStorage.changeUserEntryMen(request.id(), trimmedText);
        userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

        final LocalMessage message = new EditingAdditionalEditState().enterMessage(contextCore, request);
        request.bot().sendMessage(message, request.id());
    }
}
