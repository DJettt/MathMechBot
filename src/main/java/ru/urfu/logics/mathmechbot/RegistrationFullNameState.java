package ru.urfu.logics.mathmechbot;


import java.util.List;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.enums.RegistrationStateList;
import ru.urfu.logics.mathmechbot.models.UserEntry;


/**
 * Состояние ожидания ввода ФИО во время регистрации.
 */
public final class RegistrationFullNameState extends MathMechBotState {
    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
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
            case BACK_COMMAND -> backCommandHandler(msg, chatId, bot);
            case null -> bot.sendMessage(TRY_AGAIN, chatId);
            default -> textHandler(msg, chatId, bot);
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

    /**
     * Возвращаем пользователя на шаг назад, т.е. в основное состояние
     *
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(LocalMessage message, long chatId, Bot bot) {
        context.users.deleteById(chatId);
        context.userEntries.deleteById(chatId);
        new DefaultState(context).onEnter(message, chatId, bot);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является ФИО или ФИ (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос года обучения.
     * В противном случае просим пользователя повторить ввод.
     * @param message полученное сообщение
     * @param chatId идентификатор чата
     * @param bot бот, принявший сообщение
     */
    @SuppressWarnings("MagicNumber")
    public void textHandler(LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;
        final String trimmedText = message.text().trim();

        if (!validateFullName(trimmedText)) {
            bot.sendMessage(TRY_AGAIN, chatId);
            return;
        }

        final List<String> strs = List.of(trimmedText.split(" "));

        context.userEntries.add(new UserEntry(
                chatId, strs.get(0), strs.get(1), (strs.size() == 3) ? strs.get(2) : "",
                null, null, null, null, chatId));
        context.users.changeUserState(chatId, RegistrationStateList.YEAR);
        new RegistrationYearState(context).onEnter(message, chatId, bot);
    }
}
