package ru.urfu.logics.mathmechbot.states.registration;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.userstates.RegistrationUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса номера группы во время регистрации.
 */
public enum RegistrationGroupState implements MathMechBotState {
    INSTANCE;

    private final static Pattern VALID_GROUP_STRING_PATTERN = Pattern.compile("^[1-6]$");
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    Constants.BACK_BUTTON)))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);
            default -> textCommandHandler(context, msg, chatId, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Возвращаем пользователя на два шага назад, то есть на запрос года обучения.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, RegistrationUserState.YEAR);
        final LocalMessage message = RegistrationYearState.INSTANCE.enterMessage(context, chatId);
        bot.sendMessage(message, chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     *
     * @param context логического ядро (контекст для состояния).
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void textCommandHandler(MathMechBotCore context, LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;

        if (!VALID_GROUP_STRING_PATTERN.matcher(message.text()).matches()) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            bot.sendMessage(ON_ENTER_MESSAGE, chatId);
            return;
        }

        context.storage.userEntries.changeUserEntryGroup(chatId, Integer.parseInt(message.text()));
        context.storage.users.changeUserState(chatId, RegistrationUserState.MEN);

        final LocalMessage msg = RegistrationMenGroupState.INSTANCE.enterMessage(context, chatId);
        bot.sendMessage(msg, chatId);
    }
}
