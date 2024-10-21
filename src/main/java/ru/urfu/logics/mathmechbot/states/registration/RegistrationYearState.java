package ru.urfu.logics.mathmechbot.states.registration;


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
 * Состояние ожидания ответа на запрос года обучения во время регистрации.
 */
public final class RegistrationYearState implements MathMechBotState {
    private final Pattern validYearStringPattern = Pattern.compile("^[1-6]$");

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    backButton
            ))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> {
                request.bot().sendMessage(tryAgain, request.id());
                request.bot().sendMessage(onEnterMessage, request.id());
            }
            default -> textHandler(contextCore, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос ФИО.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_NAME);
        request.bot().sendMessage(new RegistrationFullNameState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является числом от 1 до 6 (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос специальности.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    public void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        assert request.message().text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        int year;
        try {
            year = Integer.parseInt(request.message().text().trim());
        } catch (NumberFormatException e) {
            request.bot().sendMessage(tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
            return;
        }

        if (validYearStringPattern.matcher(request.message().text()).matches()) {
            userEntryStorage.changeUserEntryYear(request.id(), year);
            userStorage.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_SPECIALTY);

            final LocalMessage msg = new RegistrationSpecialtyState().enterMessage(contextCore, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
        }
    }
}
