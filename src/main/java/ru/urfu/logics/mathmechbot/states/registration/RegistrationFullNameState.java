package ru.urfu.logics.mathmechbot.states.registration;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
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
 * Состояние ожидания ввода ФИО во время регистрации.
 */
public final class RegistrationFullNameState implements MathMechBotState {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final Pattern validFullNamePattern =
            Pattern.compile("^[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?\\s+"
                    + "[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?"
                    + "(\\s+[А-ЯЁ][а-яё]+)?$");

    /**
     * Проверяет корректность введенного ФИО.
     *
     * @param str ФИО (или ФИ).
     * @return корректна ли строка.
     */
    public boolean validateFullName(String str) {
        return validFullNamePattern.matcher(str).matches();
    }

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
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        return new LocalMessageBuilder()
                .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
                .buttons(List.of(new LocalButton("Отменить регистрацию", Constants.BACK_COMMAND)))
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
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);
        userEntryOptional.ifPresent(contextCore.getStorage().getUserEntries()::delete);
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.DEFAULT);
        bot.sendMessage(new DefaultState().enterMessage(contextCore, chatId, message, bot), chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является ФИО или ФИ (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос года обучения.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    public void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                            @NotNull LocalMessage message, @NotNull Bot bot) {
        assert message.text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final String trimmedText = message.text().trim();

        if (!validateFullName(trimmedText)) {
            bot.sendMessage(tryAgain, chatId);
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));
        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;

        userEntryStorage.add(new UserEntry(
                chatId, strings.get(0), strings.get(1), (hasPatronym) ? strings.get(2) : null,
                null, null, null, null, chatId));
        userStorage.changeUserState(chatId, MathMechBotUserState.REGISTRATION_YEAR);

        final LocalMessage msg = new RegistrationYearState().enterMessage(contextCore, chatId, message, bot);
        bot.sendMessage(msg, chatId);
    }
}
