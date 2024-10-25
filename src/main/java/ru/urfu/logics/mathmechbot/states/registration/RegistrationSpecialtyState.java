package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
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

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    /**
     * Достаёт год пользователя из хранилища.
     *
     * @param contextCore контекст.
     * @param chatId идентификатор чата
     * @return год для записи данного пользователя.
     */
    private int getUserEntryYear(@NotNull MathMechBotCore contextCore, @NotNull Long chatId) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);

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
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case null -> {
                bot.sendMessage(tryAgain, chatId);
                bot.sendMessage(enterMessage(contextCore, chatId, message, bot), chatId);
            }
            default -> textHandler(contextCore, chatId, message, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties(getUserEntryYear(contextCore, chatId))) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(backButton);
        return new LocalMessageBuilder().text("На каком направлении?").buttons(buttons).build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос года обучения.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.REGISTRATION_YEAR);
        bot.sendMessage(new RegistrationYearState().enterMessage(contextCore, chatId, message, bot), chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * В частности, проверяем, что пользователь отправил аббревиатуру одной из разрешённых специальностей.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                             @NotNull LocalMessage message, @NotNull Bot bot) {
        assert message.text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        if (!allowedSpecialties(getUserEntryYear(contextCore, chatId))
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(message.text())) {
            bot.sendMessage(tryAgain, chatId);
            bot.sendMessage(enterMessage(contextCore, chatId, message, bot), chatId);
            return;
        }

        userEntryStorage.changeUserEntrySpecialty(chatId, message.text());
        userStorage.changeUserState(chatId, MathMechBotUserState.REGISTRATION_GROUP);
        bot.sendMessage(new RegistrationGroupState().enterMessage(contextCore, chatId, message, bot), chatId);
    }
}
