package ru.urfu.logics.mathmechbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.enums.Process;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;
import ru.urfu.models.UserEntry;

import java.util.ArrayList;
import java.util.List;

public class RegistrationConfirmationState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationConfirmationState.class);

    public RegistrationConfirmationState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.MEN);
                final State newState = new RegistrationMenGroupState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case ACCEPT_COMMAND -> {
                context.users.getById(chatId).setCurrentProcess(Process.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Сохранил...").build(), chatId);
                final State newState = new DefaultState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case DECLINE_COMMAND -> {
                context.users.getById(chatId).setCurrentProcess(Process.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Отмена...").build(), chatId);
                final State newState = new DefaultState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null, default -> bot.sendMessage(TRY_AGAIN, chatId);
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        final UserEntry userEntry = context.userEntries.getById(chatId);

        if (userEntry == null) {
            LOGGER.error("User without entry reached registration end");
            return;
        }

        final String userInfo = USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        final LocalMessage message = new LocalMessageBuilder()
                .text(userInfo)
                .buttons(new ArrayList<>(List.of(
                        new LocalButton("Да", ACCEPT_COMMAND),
                        new LocalButton("Неа", DECLINE_COMMAND),
                        new LocalButton("Назад", BACK_COMMAND)
                )))
                .build();
        bot.sendMessage(message, chatId);
    }
}
