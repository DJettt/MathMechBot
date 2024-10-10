package ru.urfu.logics.mathmechbot.states.registration;


import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.userstates.DefaultUserState;
import ru.urfu.logics.mathmechbot.models.userstates.RegistrationUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания ввода ФИО во время регистрации.
 */
public enum RegistrationFullNameState implements MathMechBotState {
    INSTANCE;

    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

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
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);
            default -> textHandler(context, msg, chatId, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        return new LocalMessageBuilder()
                .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
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
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(chatId);
        context.storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
        userEntryOptional.ifPresent(context.storage.userEntries::delete);
        bot.sendMessage(DefaultState.INSTANCE.enterMessage(context, chatId), chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является ФИО или ФИ (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос года обучения.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param context логического ядро (контекст для состояния).
     * @param message полученное сообщение
     * @param chatId идентификатор чата
     * @param bot бот, принявший сообщение
     */
    public void textHandler(MathMechBotCore context, LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;
        final String trimmedText = message.text().trim();

        if (!validateFullName(trimmedText)) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            return;
        }

        final List<String> strings = List.of(trimmedText.split(" "));
        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;

        context.storage.userEntries.add(new UserEntry(
                chatId, strings.get(0), strings.get(1), (hasPatronym) ? strings.get(2) : "",
                null, null, null, null, chatId));
        context.storage.users.changeUserState(chatId, RegistrationUserState.YEAR);

        final LocalMessage msg = RegistrationYearState.INSTANCE.enterMessage(context, chatId);
        bot.sendMessage(msg, chatId);
    }
}
