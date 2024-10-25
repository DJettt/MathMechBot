package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;

/**
 * Состояние изменения курса обучения.
 */
public final class EditingYearState implements MathMechBotState {
    private final Pattern validYearStringPattern = Pattern.compile("^[1-6]$");

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    backButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case null -> bot.sendMessage(tryAgain, chatId);
            default -> textHandler(contextCore, chatId, message, bot);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param contextCore ядро
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.EDITING_CHOOSE);
        bot.sendMessage(new EditingChooseState().enterMessage(contextCore, chatId, message, bot), chatId);
    }

    /**
     * Обработка входящей информации от пользователя.
     *
     * @param contextCore логическое ядро
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                             @NotNull LocalMessage message, @NotNull Bot bot) {
        assert message.text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        int year;
        try {
            year = Integer.parseInt(message.text().trim());
        } catch (NumberFormatException e) {
            bot.sendMessage(tryAgain, chatId);
            bot.sendMessage(onEnterMessage, chatId);
            return;
        }

        if (validYearStringPattern.matcher(message.text()).matches()) {
            userEntryStorage.changeUserEntryYear(chatId, year);
            userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

            final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, chatId, message, bot);
            bot.sendMessage(msg, chatId);
        } else {
            bot.sendMessage(tryAgain, chatId);
            bot.sendMessage(onEnterMessage, chatId);
        }
    }
}
