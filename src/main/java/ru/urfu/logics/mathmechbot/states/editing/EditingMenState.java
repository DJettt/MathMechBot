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
 * Состояние изменения академической группы (МЕН-123456).
 */
public final class EditingMenState implements MathMechBotState {
    private final Pattern validMenGroupString = Pattern.compile("^МЕН-\\d{6}$");

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case null -> bot.sendMessage(tryAgain, chatId);
            default -> textHandler(contextCore, chatId, message, bot);
        }
    }

    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        return new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(List.of(backButton))
                .build();
    }

    /**
     * Возвращает пользователя на шаг назад, то есть на этап выбора, что конкретно изменить.
     *
     * @param contextCore контекст состояния.
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
     * Обработчик текстовых сообщений.
     *
     * @param contextCore контекст состояния.
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                             @NotNull LocalMessage message, @NotNull Bot bot) {
        assert message.text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final String trimmedText = message.text().trim();
        if (!validMenGroupString.matcher(trimmedText).matches()) {
            bot.sendMessage(tryAgain, chatId);
            return;
        }

        userEntryStorage.changeUserEntryMen(chatId, trimmedText);
        userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_ADDITIONAL_EDIT);

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, chatId, message, bot);
        bot.sendMessage(msg, chatId);
    }
}
