package ru.urfu.logics.mathmechbot.states.deletion;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.enums.DefaultUserState;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public final class DeletionConfirmationState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeletionConfirmationState.class);

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public DeletionConfirmationState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> {
                context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
                bot.sendMessage(new DefaultState(context).enterMessage(chatId), chatId);
            }

            case Constants.ACCEPT_COMMAND -> {
                context.storage.userEntries.deleteById(chatId);
                context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), chatId);
                bot.sendMessage(new DefaultState(context).enterMessage(chatId), chatId);
            }

            case Constants.DECLINE_COMMAND -> {
                context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
                bot.sendMessage(new LocalMessageBuilder().text("Отмена...").build(), chatId);
                bot.sendMessage(new DefaultState(context).enterMessage(chatId), chatId);
            }

            case null, default -> bot.sendMessage(Constants.TRY_AGAIN, chatId);
        }
    }

    @Override
    public LocalMessage enterMessage(long userId) {
        final UserEntry userEntry = context.storage.userEntries.getById(userId);

        if (userEntry == null) {
            LOGGER.error("User without entry reached deletion confirmation state");
            return null;
        }

        final String userInfo = Constants.USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userInfo)
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
    }
}
