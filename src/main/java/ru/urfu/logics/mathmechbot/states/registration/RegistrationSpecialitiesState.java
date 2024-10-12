package ru.urfu.logics.mathmechbot.states.registration;

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
import ru.urfu.logics.mathmechbot.models.Specialty;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса направления подготовки.
 * Предлагает пользователю направление подготовки
 * из списка, который возвращает метод allowedSpecialties.
 */
public sealed interface RegistrationSpecialitiesState
        extends MathMechBotState
        permits RegistrationFirstYearSpecialtiesState, RegistrationLaterYearSpecialitiesState {

    /**
     * Возвращает список разрешённых специальностей.
     * Нужно для наследования от этого класса.
     *
     * @return список разрешённых специальностей
     */
    @NotNull
    default List<Specialty> allowedSpecialties() {
        return new ArrayList<>() {
        };
    }

    @Override
    default void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> {
                request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
                request.bot().sendMessage(enterMessage(context, request), request.id());
            }
            default -> textHandler(context, request);
        }
    }

    @Override
    @NotNull
    default LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties()) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(Constants.BACK_BUTTON);

        return new LocalMessageBuilder().text("На каком направлении?").buttons(buttons).build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос года обучения.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_YEAR);
        request.bot().sendMessage(RegistrationYearState.INSTANCE.enterMessage(context, request), request.id());
    }


    /**
     * Проверяем различные текстовые сообщения.
     * В частности, проверяем, что пользователь отправил аббревиатуру одной из разрешённых специальностей.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    default void textHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        assert request.message().text() != null;

        if (!allowedSpecialties()
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(request.message().text())) {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            request.bot().sendMessage(enterMessage(context, request), request.id());
            return;
        }

        context.storage.userEntries.changeUserEntrySpecialty(request.id(), request.message().text());
        context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_GROUP);
        request.bot().sendMessage(RegistrationGroupState.INSTANCE.enterMessage(context, request), request.id());
    }
}
