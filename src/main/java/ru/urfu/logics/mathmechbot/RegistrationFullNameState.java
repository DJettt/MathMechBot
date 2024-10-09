package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.enums.Process;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;
import ru.urfu.models.UserEntry;

import java.util.List;


public class RegistrationFullNameState extends MathMechBotState {
    public RegistrationFullNameState(MathMechBotCore context) {
        super(context);
    }

    /**
     * Проверяет корректность введенного ФИО.
     *
     * @param str ФИО (или ФИ).
     * @return корректна ли строка.
     */
    public boolean validateFullName(String str) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        return str.matches("^[А-Я][а-я]+ [А-Я][а-я]+( [А-Я][а-я]+)?$");
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.getById(chatId).setCurrentProcess(Process.DEFAULT);
                final State newState = new DefaultState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                final String trimmedText = msg.text().trim();

                if (!validateFullName(trimmedText)) {
                    bot.sendMessage(TRY_AGAIN, chatId);
                    return;
                }
                final List<String> strs = List.of(trimmedText.split(" "));
                context.userEntries.add(new UserEntry(
                        chatId, strs.get(1), strs.get(0), (strs.size() == 3) ? strs.get(2) : "",
                        null, null, null, null, chatId));
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                final State newState = new RegistrationYearState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        final LocalMessage message = new LocalMessageBuilder()
                .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
                .build();
        bot.sendMessage(message, chatId);
    }
}
