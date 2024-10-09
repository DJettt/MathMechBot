package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;

import java.util.ArrayList;
import java.util.List;


/**
 * Состояние ожидания ввода академической группы в формате МЕН во время регистрации.
 */
public class RegistrationMenGroupState extends MathMechBotState {
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
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                final State newState = new RegistrationGroupState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                final String trimmedText = msg.text().trim();

                if (!validateGroup(trimmedText)) {
                    bot.sendMessage(TRY_AGAIN, chatId);
                    return;
                }

                context.userEntries.getById(chatId).setMen(msg.text());
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.CONFIRMATION);
                final State newState = new RegistrationConfirmationState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
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
