package ru.urfu.logics.mathmechbot.states.editing;

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
 * Состояние изменения курса обучения.
 */
public enum EditingYearState implements MathMechBotState {
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
            case null -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            default -> textHandler(context, request);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Обработка кнопки "назад".
     * @param context ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.storage.getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(EditingChooseState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Обработка входящей информации от пользователя.
     *
     * @param context логическое ядро
     * @param request запрос
     */
    private void textHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        assert request.message().text() != null;

        int year;
        try {
            year = Integer.parseInt(request.message().text().trim());
        } catch (NumberFormatException e) {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
            return;
        }

        if (VALID_YEAR_STRING_PATTERN.matcher(request.message().text()).matches()) {
            context.storage.getUserEntries().changeUserEntryYear(request.id(), year);
            context.storage.getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

            final LocalMessage msg = EditingAdditionalEditState.INSTANCE.enterMessage(context, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(ON_ENTER_MESSAGE, request.id());
        }
    }
}
