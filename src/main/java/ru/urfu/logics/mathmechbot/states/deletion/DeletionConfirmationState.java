package ru.urfu.logics.mathmechbot.states.deletion;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Состояние ожидания подтверждения удаления данных.
 */
public final class DeletionConfirmationState implements MathMechBotState {
    private final Logger logger = LoggerFactory.getLogger(DeletionConfirmationState.class);

    private final LocalButton yesButton = new LocalButton("Да", Constants.ACCEPT_COMMAND);
    private final LocalButton noButton = new LocalButton("Нет", Constants.DECLINE_COMMAND);
    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case Constants.ACCEPT_COMMAND -> acceptCommandHandler(contextCore, chatId, message, bot);
            case Constants.DECLINE_COMMAND -> declineCommandHandler(contextCore, chatId, message, bot);
            case null, default -> {
                bot.sendMessage(tryAgain, chatId);
                bot.sendMessage(enterMessage(contextCore, chatId, message, bot), chatId);
            }
        }
    }

    @Override
    @Nullable
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry reached deletion confirmation state");
            return null;
        }

        return new LocalMessageBuilder()
                .text("Точно удаляем?\n\n" + userEntryOptional.get().toHumanReadable())
                .buttons(List.of(yesButton, noButton, backButton))
                .build();
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.DEFAULT);
        bot.sendMessage(new DefaultState().enterMessage(contextCore, chatId, message, bot), chatId);
    }

    /**
     * Обрабатывает команду согласия: удаляет данные пользователя, переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void acceptCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                      @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final Optional<UserEntry> userEntryOptional = userEntryStorage.get(chatId);
        userEntryOptional.ifPresent(userEntryStorage::delete);

        userStorage.changeUserState(chatId, MathMechBotUserState.DEFAULT);
        bot.sendMessage(new LocalMessage("Удаляем..."), chatId);
        bot.sendMessage(new DefaultState().enterMessage(contextCore, chatId, message, bot), chatId);
    }

    /**
     * Обрабатывает команду несогласия: переносит в дефолтное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void declineCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                       @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.DEFAULT);
        bot.sendMessage(new LocalMessage("Отмена..."), chatId);
        bot.sendMessage(new DefaultState().enterMessage(contextCore, chatId, message, bot), chatId);
    }
}
