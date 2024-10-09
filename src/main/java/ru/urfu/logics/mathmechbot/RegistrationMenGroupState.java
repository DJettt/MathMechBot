package ru.urfu.logics.mathmechbot;


import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.enums.RegistrationStateList;


/**
 * Состояние ожидания ввода академической группы в формате МЕН во время регистрации.
 */
public class RegistrationMenGroupState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistrationMenGroupState.class);

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationMenGroupState(MathMechBotCore context) {
        super(context);
    }

    /**
     * Проверяет корректность введённой академической группы.
     *
     * @param str строка с академической группой в формате МЕН.
     * @return корректна ли строка.
     */
    public boolean validateGroup(String str) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        return str.matches("^МЕН-\\d{6}$");
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.changeUserState(chatId, RegistrationStateList.GROUP);
                new RegistrationGroupState(context).onEnter(msg, chatId, bot);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                final String trimmedText = msg.text().trim();

                if (!validateGroup(trimmedText)) {
                    bot.sendMessage(TRY_AGAIN, chatId);
                    return;
                }

                context.userEntries.changeUserEntryMen(chatId, msg.text());
                context.users.changeUserState(chatId, RegistrationStateList.CONFIRMATION);
                new RegistrationConfirmationState(context).onEnter(msg, chatId, bot);
            }
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        final LocalMessage message = new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(new ArrayList<>(List.of(BACK_BUTTON)))
                .build();
        bot.sendMessage(message, chatId);
    }
}
