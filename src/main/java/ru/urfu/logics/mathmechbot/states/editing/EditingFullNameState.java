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
 * Состояние изменения ФИО.
 */
public final class EditingFullNameState implements MathMechBotState {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(backButton))
            .build();
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final Pattern validFullNamePattern =
            Pattern.compile("^[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?\\s+"
                    + "[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?"
                    + "(\\s+[А-ЯЁ][а-яё]+)?$");

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
     * Проверка корректности ФИО (или ФИ).
     * @param str ФИО
     * @return строка корректна или нет
     */
    public boolean validateFullName(String str) {
        return validFullNamePattern.matcher(str).matches();
    }

    /**
     * Обработка текста.
     * @param contextCore ядро
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                             @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        assert message.text() != null;
        final String trimmedText = message.text().trim();

        if (!validateFullName(trimmedText)) {
            bot.sendMessage(tryAgain, chatId);
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));

        userEntryStorage.changeUserEntrySurname(chatId, strings.get(0));
        userEntryStorage.changeUserEntryName(chatId, strings.get(1));
        if (strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM) {
            userEntryStorage.changeUserEntryPatronym(chatId, strings.get(2));
        } else {
            userEntryStorage.changeUserEntryPatronym(chatId, null);
        }
        userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_ADDITIONAL_EDIT);
        bot.sendMessage(new LocalMessage("Данные сохранены."), chatId);

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, chatId, message, bot);
        bot.sendMessage(msg, chatId);
    }
}
