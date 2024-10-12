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

    private final static Pattern VALID_YEAR_STRING_PATTERN = Pattern.compile("^[1-6]$");
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    Constants.BACK_BUTTON
            )))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> {
                request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
                request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
            }
            default -> textHandler(context, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос ФИО.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_NAME);
        request.bot().sendMessage(RegistrationFullNameState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является числом от 1 до 6 (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос специальности.
     * Если год обучения - 1, отправляем в запрос специальности первокурсника (так как они отличаются от других курсов).
     * Если год обучения иной, отправляем в запрос специальности поздних курсов.
     * В противном случае просим пользователя повторить ввод.
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
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
            return;
        }

        if (request.message().text().equals("1")) {
            context.storage.userEntries.changeUserEntryYear(request.id(), year);
            context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_SPECIALTY1);

            final LocalMessage msg = RegistrationFirstYearSpecialtiesState.INSTANCE.enterMessage(context, request);
            request.bot().sendMessage(msg, request.id());
        } else if (VALID_YEAR_STRING_PATTERN.matcher(request.message().text()).matches()) {
            context.storage.userEntries.changeUserEntryYear(request.id(), year);
            context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_SPECIALTY2);

            final LocalMessage msg = RegistrationLaterYearSpecialitiesState.INSTANCE.enterMessage(context, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
        }
    }
}
