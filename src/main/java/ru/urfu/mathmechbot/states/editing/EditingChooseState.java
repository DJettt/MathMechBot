package ru.urfu.mathmechbot.states.editing;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.states.DefaultState;
import ru.urfu.mathmechbot.states.MathMechBotState;

/**
 * Состояние в котором пользователь выбирает, какую информацию он хочет изменить.
 */
public final class EditingChooseState extends MathMechBotState {
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
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context(), request);
            case EDITING_FULL_NAME -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_FULL_NAME);
                request.bot().sendMessage(new EditingFullNameState().enterMessage(context(), request), request.id());
            }
            case EDITING_YEAR -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_YEAR);
                request.bot().sendMessage(new EditingYearState().enterMessage(context(), request), request.id());
            }
            case EDITING_SPECIALITY -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_SPECIALITY);
                request.bot().sendMessage(new EditingSpecialityState().enterMessage(context(), request), request.id());
            }
            case EDITING_GROUP -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_GROUP);
                request.bot().sendMessage(new EditingGroupState().enterMessage(context(), request), request.id());
            }
            case EDITING_MEN -> {
                context().getStorage().getUsers().changeUserState(request.id(),
                        MathMechBotUserState.EDITING_MEN);
                request.bot().sendMessage(new EditingMenState().enterMessage(context(), request), request.id());
            }
            default -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Обработка кнопки "назад".
     * @param context логическое ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new DefaultState().enterMessage(context, request), request.id());
    }
}
