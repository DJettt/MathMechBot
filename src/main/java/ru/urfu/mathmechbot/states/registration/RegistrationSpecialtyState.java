package ru.urfu.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса направления подготовки.
 * Предлагает пользователю направление подготовки
 * из списка, который возвращает метод allowedSpecialties.
 */
public final class RegistrationSpecialtyState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationSpecialtyState.class);

    /**
     * Достаёт год пользователя из хранилища.
     *
     * @param context контекст.
     * @param request запрос.
     * @return год для записи данного пользователя.
     */
    private int getUserEntryYear(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(request.id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry managed to reach registration_specialty state.");
            throw new RuntimeException();
        } else if (userEntryOptional.get().year() == null) {
            LOGGER.error("User without set year managed to reach registration_specialty state.");
            throw new RuntimeException();
        }

        return userEntryOptional.get().year();
    }

    /**
     * Возвращает список разрешённых специальностей.
     * Нужно для наследования от этого класса.
     *
     * @param year год обучения данного студента.
     * @return список разрешённых специальностей.
     */
    @NotNull
    private List<Specialty> allowedSpecialties(int year) {
        if (year == 1) {
            return new ArrayList<>(List.of(
                    Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
            ));
        }
        return new ArrayList<>(List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT
        ));
    }

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context(), request);
            case null -> {
                request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
                request.bot().sendMessage(enterMessage(context(), request), request.id());
            }
            default -> textHandler(context(), request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties(getUserEntryYear(context, request))) {
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
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        context.storage.users.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_YEAR);
        request.bot().sendMessage(new RegistrationYearState().enterMessage(context, request), request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     * В частности, проверяем, что пользователь отправил аббревиатуру одной из разрешённых специальностей.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void textHandler(@NotNull MathMechBotCore context, @NotNull BotProcessMessageRequest request) {
        assert request.message().text() != null;

        if (!allowedSpecialties(getUserEntryYear(context, request))
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
        request.bot().sendMessage(new RegistrationGroupState().enterMessage(context, request), request.id());
    }
}
