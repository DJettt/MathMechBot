package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;

import java.util.ArrayList;
import java.util.List;


public class RegistrationGroupState extends MathMechBotState {
    public RegistrationGroupState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                final State newState = new RegistrationYearState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                try {
                    final int group = Integer.parseInt(msg.text().trim());
                    if (group >= 1 && group <= 5) {
                        context.userEntries.getById(chatId).setGroup(String.valueOf(group));
                        context.users.getById(chatId).setCurrentState(RegistrationProcessState.MEN);
                        final State newState = new RegistrationMenGroupState(context);
                        newState.onEnter(msg, chatId, bot);
                        context.changeState(newState);
                    } else {
                        bot.sendMessage(TRY_AGAIN, chatId);
                    }
                } catch (NumberFormatException e) {
                    bot.sendMessage(TRY_AGAIN, chatId);
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
                        BACK_BUTTON
                )))
                .build();
        bot.sendMessage(message, chatId);
    }
}