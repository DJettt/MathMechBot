package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.enums.DefaultState;
import ru.urfu.logics.mathmechbot.enums.RegistrationState;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние подтверждения введённых данных во время регистрации.
 */
public final class RegistrationConfirmationState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationConfirmationState.class);

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationConfirmationState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> {
                context.storage.users.changeUserState(chatId, RegistrationState.MEN);
                new RegistrationMenGroupState(context).onEnter(msg, chatId, bot);
            }

            case Constants.ACCEPT_COMMAND -> {
                context.storage.users.changeUserState(chatId, DefaultState.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Сохранил...").build(), chatId);
                new ru.urfu.logics.mathmechbot.states.DefaultState(context).onEnter(msg, chatId, bot);
            }

            case Constants.DECLINE_COMMAND -> {
                context.storage.users.changeUserState(chatId, DefaultState.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Отмена...").build(), chatId);
                new ru.urfu.logics.mathmechbot.states.DefaultState(context).onEnter(msg, chatId, bot);
            }

            case null, default -> bot.sendMessage(Constants.TRY_AGAIN, chatId);
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        final UserEntry userEntry = context.storage.userEntries.getById(chatId);

        if (userEntry == null) {
            LOGGER.error("User without entry reached registration end");
            return;
        }

        final String userInfo = Constants.USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        final LocalMessage message = new LocalMessageBuilder()
                .text(userInfo)
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
        bot.sendMessage(message, chatId);
    }
}
