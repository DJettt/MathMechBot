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
 * Состояние изменения курса обучения.
 */
public final class EditingYearState implements MathMechBotState {
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
                    backButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> request.bot().sendMessage(tryAgain, request.id());
            default -> textHandler(contextCore, request);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param contextCore ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(new EditingChooseState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Обработка входящей информации от пользователя.
     *
     * @param contextCore логическое ядро
     * @param request запрос
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
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
            userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

            final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(tryAgain, request.id());
            request.bot().sendMessage(onEnterMessage, request.id());
        }
    }
}
