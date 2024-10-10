package ru.urfu.logics.mathmechbot;


import java.util.ArrayList;
import java.util.List;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.enums.RegistrationStateList;


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
                context.users.changeUserState(chatId, RegistrationStateList.YEAR);
                new RegistrationYearState(context).onEnter(msg, chatId, bot);
            }

            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);

            default -> {
                final int maxYear = 5;
                try {
                    final int group = Integer.parseInt(msg.text().trim());
                    if (group >= 1 && group <= maxYear) {
                        context.userEntries.changeUserEntryGroup(chatId, group);
                        context.users.changeUserState(chatId, RegistrationStateList.MEN);
                        new RegistrationMenGroupState(context).onEnter(msg, chatId, bot);
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
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        final LocalMessage message = new LocalMessageBuilder()
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
        bot.sendMessage(message, chatId);
    }
}
