package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.enums.RegistrationState;
import ru.urfu.logics.mathmechbot.enums.Specialty;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса направления подготовки.
 * Предлагает пользователю направление подготовки
 * из списка, который возвращает метод allowedSpecialties.
 */
public sealed class RegistrationSpecialitiesState
        extends MathMechBotState
        permits RegistrationFirstYearSpecialtiesState, RegistrationLaterYearSpecialitiesState {
    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationSpecialitiesState(MathMechBotCore context) {
        super(context);
    }

    /**
     * Возвращает список разрешённых специальностей.
     * Нужно для наследования от этого класса.
     *
     * @return список разрешённых специальностей
     */
    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>() {
        };
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(msg, chatId, bot);
            case null -> {
                bot.sendMessage(Constants.TRY_AGAIN, chatId);
                onEnter(msg, chatId, bot);
            }
            default -> textHandler(msg, chatId, bot);
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties()) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(Constants.BACK_BUTTON);

        final LocalMessage message = new LocalMessageBuilder().text("На каком направлении?").buttons(buttons).build();

        bot.sendMessage(message, chatId);
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос года обучения.
     *
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(LocalMessage message, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, RegistrationState.YEAR);
        new RegistrationYearState(context).onEnter(message, chatId, bot);
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
    public void textHandler(LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;

        if (!allowedSpecialties().stream().map(Specialty::getAbbreviation).toList().contains(message.text())) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            onEnter(message, chatId, bot);
            return;
        }

        context.storage.userEntries.changeUserEntrySpecialty(chatId, message.text());
        context.storage.users.changeUserState(chatId, RegistrationState.GROUP);
        new RegistrationGroupState(context).onEnter(message, chatId, bot);
    }
}
