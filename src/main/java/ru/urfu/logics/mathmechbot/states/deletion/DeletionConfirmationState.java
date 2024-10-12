package ru.urfu.logics.mathmechbot.states.deletion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.userstates.DefaultUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public enum DeletionConfirmationState implements MathMechBotState {
    INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(DeletionConfirmationState.class);

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(context, chatId, bot);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(context, chatId, bot);
            case null, default -> {
                bot.sendMessage(Constants.TRY_AGAIN, chatId);
                bot.sendMessage(enterMessage(context, chatId), chatId);
            }
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(userId);

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry reached deletion confirmation state");
            return null;
        }
        final UserEntry userEntry = userEntryOptional.get();

        final String userInfo = Constants.USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userInfo)
                .buttons(new ArrayList<>(List.of(Constants.YES_BUTTON, Constants.NO_BUTTON, Constants.BACK_BUTTON)))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
        bot.sendMessage(DefaultState.INSTANCE.enterMessage(context, chatId), chatId);
    }

    /**
     * Обрабатывает команду согласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void acceptCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(chatId);
        userEntryOptional.ifPresent(context.storage.userEntries::delete);

        context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
        bot.sendMessage(new LocalMessageBuilder().text("Удаляем...").build(), chatId);
        bot.sendMessage(DefaultState.INSTANCE.enterMessage(context, chatId), chatId);
    }

    /**
     * Обрабатывает команду несогласия: переносит в дефолтное состояние.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void declineCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
        bot.sendMessage(new LocalMessageBuilder().text("Отмена...").build(), chatId);
        bot.sendMessage(DefaultState.INSTANCE.enterMessage(context, chatId), chatId);
    }
}
