package ru.urfu.logics.mathmechbot.states.registration;


import java.util.ArrayList;
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


/**
 * Состояние ожидания ответа на запрос года обучения во время регистрации.
 */
public enum RegistrationYearState implements MathMechBotState {
    INSTANCE;

    private final Pattern validYearStringPattern = Pattern.compile("^[1-6]$");
    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    new Constants().backButton
            )))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> {
                request.bot().sendMessage(new Constants().tryAgain, request.id());
                request.bot().sendMessage(onEnterMessage, request.id());
            }
            default -> textHandler(context, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос ФИО.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_NAME);
        request.bot().sendMessage(RegistrationFullNameState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является числом от 1 до 6 (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос специальности.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    public void textHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        assert request.message().text() != null;

        int year;
        try {
            year = Integer.parseInt(request.message().text().trim());
        } catch (NumberFormatException e) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
            return;
        }

        if (validYearStringPattern.matcher(request.message().text()).matches()) {
            context.getStorage().getUserEntries().changeUserEntryYear(request.id(), year);
            context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_SPECIALTY);

            final LocalMessage msg = RegistrationSpecialtyState.INSTANCE.enterMessage(context, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
        }
    }
}
