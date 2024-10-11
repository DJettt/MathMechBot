package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.userstates.RegistrationUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания ввода академической группы в формате МЕН во время регистрации.
 */
public enum RegistrationMenGroupState implements MathMechBotState {
    INSTANCE;

    // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
    private final static Pattern VALID_MEN_GROUP_STRING = Pattern.compile("^МЕН-\\d{6}$");

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);
            default -> textHandler(context, msg, chatId, bot);
        }
    }

    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        return new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(new ArrayList<>(List.of(Constants.BACK_BUTTON)))
                .build();
    }

    /**
     * Обработчик команду возврата.
     *
     * @param context контекст состояния.
     * @param chatId  идентификатор отправителя.
     * @param bot     бот, принявший сообщение.
     */
    private void backCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, RegistrationUserState.GROUP);
        bot.sendMessage(RegistrationGroupState.INSTANCE.enterMessage(context, chatId), chatId);
    }

    /**
     * Обработчик текстовых сообщений.
     *
     * @param context контекст состояния.
     * @param msg     принятое сообщение.
     * @param chatId  идентификатор отправителя.
     * @param bot     бот, принявший сообщение.
     */
    private void textHandler(MathMechBotCore context, LocalMessage msg, long chatId, Bot bot) {
        assert msg.text() != null;

        final String trimmedText = msg.text().trim();

        if (!validateGroup(trimmedText)) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            return;
        }

        context.storage.userEntries.changeUserEntryMen(chatId, msg.text());
        context.storage.users.changeUserState(chatId, RegistrationUserState.CONFIRMATION);

        final LocalMessage message = RegistrationConfirmationState.INSTANCE.enterMessage(context, chatId);
        if (message != null) {
            bot.sendMessage(message, chatId);
        }
    }

    /**
     * Проверяет корректность введённой академической группы.
     *
     * @param str строка с академической группой в формате МЕН.
     * @return корректна ли строка.
     */
    public boolean validateGroup(String str) {
        return VALID_MEN_GROUP_STRING.matcher(str).matches();
    }
}
