package ru.urfu.logics.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
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
public final class EditingChooseState implements MathMechBotState {
    //TODO: поменять местоположение констант.
    private static final String EDITING_FULL_NAME = "full_name";
    private static final String EDITING_YEAR = "year";
    private static final String EDITING_SPECIALITY = "speciality";
    private static final String EDITING_GROUP = "number_of_group";
    private static final String EDITING_MEN = "men";

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME),
                    new LocalButton("Курс", EDITING_YEAR),
                    new LocalButton("Направление", EDITING_SPECIALITY),
                    new LocalButton("Группа", EDITING_GROUP),
                    new LocalButton("МЕН", EDITING_MEN),
                    new Constants().backButton)))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case EDITING_FULL_NAME -> {
                context.getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_FULL_NAME);
                request.bot().sendMessage(new EditingFullNameState().enterMessage(context, request), request.id());
            }
            case EDITING_YEAR -> {
                context.getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_YEAR);
                request.bot().sendMessage(new EditingYearState().enterMessage(context, request), request.id());
            }
            case EDITING_SPECIALITY -> {
                context.getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_SPECIALITY);
                request.bot().sendMessage(new EditingSpecialityState().enterMessage(context, request), request.id());
            }
            case EDITING_GROUP -> {
                context.getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_GROUP);
                request.bot().sendMessage(new EditingGroupState().enterMessage(context, request), request.id());
            }
            case EDITING_MEN -> {
                context.getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_MEN);
                request.bot().sendMessage(new EditingMenState().enterMessage(context, request), request.id());
            }
            case null, default -> request.bot().sendMessage(new Constants().tryAgain, request.id());
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param context логическое ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(DefaultState.INSTANCE.enterMessage(context, request), request.id());
    }
}
