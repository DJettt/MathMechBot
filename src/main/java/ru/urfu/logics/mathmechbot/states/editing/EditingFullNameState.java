package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
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

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(new Constants().backButton))
            .build();

    private final Pattern validFullNamePattern =
            Pattern.compile("^[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?\\s+"
                    + "[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?"
                    + "(\\s+[А-ЯЁ][а-яё]+)?$");

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> request.bot().sendMessage(new Constants().tryAgain, request.id());
            default -> textHandler(contextCore, request);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param contextCore ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(new EditingChooseState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Проверка корректности ФИО (или ФИ).
     * @param str ФИО
     * @return строка корректна или нет
     */
    public boolean validateFullName(String str) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        return validFullNamePattern.matcher(str).matches();
    }

    /**
     * Обработка текста.
     * @param contextCore ядро
     * @param request запрос
     */
    private void textHandler(MathMechBotCore contextCore, Request request) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        assert request.message().text() != null;
        final String trimmedText = request.message().text().trim();

        if (!validateFullName(trimmedText)) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));

        userEntryStorage.changeUserEntrySurname(request.id(), strings.get(0));
        userEntryStorage.changeUserEntryName(request.id(), strings.get(1));
        if (strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM) {
            userEntryStorage.changeUserEntryPatronym(request.id(), strings.get(2));
        } else {
            userEntryStorage.changeUserEntryPatronym(request.id(), null);
        }
        userStorage.changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Данные сохранены.").build(), request.id());

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(contextCore, request);
        request.bot().sendMessage(msg, request.id());
    }
}
