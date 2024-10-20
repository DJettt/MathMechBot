package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.Specialty;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Состояние запроса направления подготовки.
 * Предлагает пользователю направление подготовки
 * из списка, который возвращает метод allowedSpecialties.
 */
public final class RegistrationSpecialtyState implements MathMechBotState {
    private final Logger logger = LoggerFactory.getLogger(RegistrationSpecialtyState.class);

    /**
     * Достаёт год пользователя из хранилища.
     *
     * @param context контекст.
     * @param request запрос.
     * @return год для записи данного пользователя.
     */
    private int getUserEntryYear(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry managed to reach registration_specialty state.");
            throw new RuntimeException();
        } else if (userEntryOptional.get().year() == null) {
            logger.error("User without set year managed to reach registration_specialty state.");
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
            return List.of(
                    Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
            );
        }
        return List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT,
                Specialty.PM, Specialty.KB, Specialty.FT);
    }

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> {
                request.bot().sendMessage(new Constants().tryAgain, request.id());
                request.bot().sendMessage(enterMessage(context, request), request.id());
            }
            default -> textHandler(context, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties(getUserEntryYear(context, request))) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(new Constants().backButton);
        return new LocalMessageBuilder().text("На каком направлении?").buttons(buttons).build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос года обучения.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_YEAR);
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
    private void textHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        assert request.message().text() != null;

        final UserStorage userStorage = context.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = context.getStorage().getUserEntries();

        if (!allowedSpecialties(getUserEntryYear(context, request))
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(request.message().text())) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            request.bot().sendMessage(enterMessage(context, request), request.id());
            return;
        }

        userEntryStorage.changeUserEntrySpecialty(request.id(), request.message().text());
        userStorage.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_GROUP);
        request.bot().sendMessage(new RegistrationGroupState().enterMessage(context, request), request.id());
    }
}
