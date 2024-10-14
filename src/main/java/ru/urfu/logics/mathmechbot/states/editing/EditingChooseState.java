package ru.urfu.logics.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;

/**
 * Состояние в котором пользователь выбирает, какую информацию он хочет изменить.
 */
public enum EditingChooseState implements MathMechBotState {
    INSTANCE;

    //TODO: поменять местоположение констант.
    private final static String EDITING_FULL_NAME = "full_name";
    private final static String EDITING_YEAR = "year";
    private final static String EDITING_SPECIALITY = "speciality";
    private final static String EDITING_GROUP = "number_of_group";
    private final static String EDITING_MEN = "men";
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME),
                    new LocalButton("Курс", EDITING_YEAR),
                    new LocalButton("Направление", EDITING_SPECIALITY),
                    new LocalButton("Группа", EDITING_GROUP),
                    new LocalButton("МЕН", EDITING_MEN),
                    Constants.BACK_BUTTON)))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            default -> textCommandHandler(context, request);
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Обработка кнопки "назад".
     * @param context логическое ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.storage.users.changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Проверяем что именно хочет изменить пользователь.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void textCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        //TODO: дополнить!
        switch (request.message().text()) {
            case EDITING_FULL_NAME -> {
                context.storage.users.changeUserState(request.id(),
                        MathMechBotUserState.EDITING_FULL_NAME);
                request.bot().sendMessage(EditingFullNameState.INSTANCE.enterMessage(context, request), request.id());
            }
            default -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
        }
    }
}
