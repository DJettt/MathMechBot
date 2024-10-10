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
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса номера группы во время регистрации.
 */
public final class RegistrationGroupState extends MathMechBotState {
    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationGroupState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> {
                context.storage.users.changeUserState(chatId, RegistrationUserState.YEAR);
                bot.sendMessage(new RegistrationYearState(context).enterMessage(chatId), chatId);
            }

            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);

            default -> {
                final int maxYear = 5;
                try {
                    final int group = Integer.parseInt(msg.text().trim());
                    if (group >= 1 && group <= maxYear) {
                        context.storage.userEntries.changeUserEntryGroup(chatId, group);
                        context.storage.users.changeUserState(chatId, RegistrationUserState.MEN);
                        bot.sendMessage(new RegistrationMenGroupState(context).enterMessage(chatId), chatId);
                    } else {
                        bot.sendMessage(Constants.TRY_AGAIN, chatId);
                    }
                } catch (NumberFormatException e) {
                    bot.sendMessage(Constants.TRY_AGAIN, chatId);
                }
            }
        }
    }

    @Override
    public LocalMessage enterMessage(long userId) {
        return new LocalMessageBuilder()
                .text("На каком курсе Вы обучаетесь?")
                .buttons(new ArrayList<>(List.of(
                        new LocalButton("1 группа", "1"),
                        new LocalButton("2 группа", "2"),
                        new LocalButton("3 группа", "3"),
                        new LocalButton("4 группа", "4"),
                        new LocalButton("5 группа", "5"),
                        Constants.BACK_BUTTON
                )))
                .build();
    }
}
