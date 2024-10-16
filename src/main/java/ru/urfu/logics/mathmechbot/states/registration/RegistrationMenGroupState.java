package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
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


/**
 * Состояние ожидания ввода академической группы в формате МЕН во время регистрации.
 */
public enum RegistrationMenGroupState implements MathMechBotState {
    INSTANCE;

    // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
    private final static Pattern VALID_MEN_GROUP_STRING = Pattern.compile("^МЕН-\\d{6}$");

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            default -> textHandler(context, request);
        }
    }

    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(new ArrayList<>(List.of(Constants.BACK_BUTTON)))
                .build();
    }

    /**
     * Возвращает пользователя на шаг назад, то есть на этап запроса группы.
     *
     * @param context контекст состояния.
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_GROUP);
        request.bot().sendMessage(RegistrationGroupState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Обработчик текстовых сообщений.
     *
     * @param context контекст состояния.
     * @param request запрос.
     */
    private void textHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        assert request.message().text() != null;

        final String trimmedText = request.message().text().trim();
        if (!VALID_MEN_GROUP_STRING.matcher(trimmedText).matches()) {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            return;
        }

        context.getStorage().getUserEntries().changeUserEntryMen(request.id(), trimmedText);
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_CONFIRMATION);

        final LocalMessage message = RegistrationConfirmationState.INSTANCE.enterMessage(context, request);
        if (message != null) {
            request.bot().sendMessage(message, request.id());
        }
    }
}
