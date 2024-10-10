package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.enums.RegistrationUserState;
import ru.urfu.logics.mathmechbot.enums.Specialty;
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
    default List<Specialty> allowedSpecialties() {
        return new ArrayList<>() {
        };
    }

    @Override
    default void processMessage(MathMechBotCore context, LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case null -> {
                bot.sendMessage(Constants.TRY_AGAIN, chatId);
                bot.sendMessage(enterMessage(context, chatId), chatId);
            }
            default -> textHandler(context, msg, chatId, bot);
        }
    }

    @Override
    default LocalMessage enterMessage(MathMechBotCore context, long userId) {
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
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, RegistrationUserState.YEAR);
        bot.sendMessage(RegistrationYearState.INSTANCE.enterMessage(context, chatId), chatId);
    }


    /**
     * Проверяем различные текстовые сообщения.
     * В частности, проверяем, что пользователь отправил аббревиатуру одной из разрешённых специальностей.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    default void textHandler(MathMechBotCore context, LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;

        if (!allowedSpecialties().stream().map(Specialty::getAbbreviation).toList().contains(message.text())) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            bot.sendMessage(enterMessage(context, chatId), chatId);
            return;
        }

        context.storage.userEntries.changeUserEntrySpecialty(chatId, message.text());
        context.storage.users.changeUserState(chatId, RegistrationUserState.GROUP);
        bot.sendMessage(RegistrationGroupState.INSTANCE.enterMessage(context, chatId), chatId);
    }
}
