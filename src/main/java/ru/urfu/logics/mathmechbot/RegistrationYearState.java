package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;

import java.util.ArrayList;
import java.util.List;


public class RegistrationYearState extends MathMechBotState {
    public RegistrationYearState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.NAME);
                final State newState = new RegistrationFullNameState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                try {
                    final int year = Integer.parseInt(msg.text().trim());
                    if (year == 1) {
                        context.userEntries.getById(chatId).setYear(String.valueOf(year));
                        context.users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY1);

                        final State newState = new RegistrationFirstYearSpecialtiesState(context);
                        newState.onEnter(msg, chatId, bot);
                        context.changeState(newState);
                    } else if (year > 1 && year <= 6) {
                        context.userEntries.getById(chatId).setYear(String.valueOf(year));
                        context.users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY2);

                        final State newState = new RegistrationLaterYearSpecialitiesState(context);
                        newState.onEnter(msg, chatId, bot);
                        context.changeState(newState);
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
                        new LocalButton("1 курс", "1"),
                        new LocalButton("2 курс", "2"),
                        new LocalButton("3 курс", "3"),
                        new LocalButton("4 курс", "4"),
                        new LocalButton("5 курс", "5"),
                        new LocalButton("6 курс", "6"),
                        BACK_BUTTON
                )))
                .build();
        bot.sendMessage(message, chatId);
    }
}
