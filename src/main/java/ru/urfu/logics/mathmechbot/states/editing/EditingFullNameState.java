package ru.urfu.logics.mathmechbot.states.editing;

import java.util.ArrayList;
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

/**
 * Состояние изменения ФИО.
 */
public enum EditingFullNameState implements MathMechBotState {
    INSTANCE;

    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(new ArrayList<>(List.of(Constants.BACK_BUTTON)))
            .build();
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;
    private final static Pattern VALID_FULL_NAME_PATTERN =
            Pattern.compile("^[А-ЯЁ][а-яё]+\\s+[А-ЯЁ][а-яё]+(\\s+[А-ЯЁ][а-яё]+)?$");

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            default -> textHandler(context, request);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Обработка кнопки "назад".
     * @param context ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.storage.getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(EditingChooseState.INSTANCE.enterMessage(context, request), request.id());
    }

    /**
     * Проверка корректности ФИО (или ФИ).
     * @param str ФИО
     * @return строка корректна или нет
     */
    public boolean validateFullName(String str) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        return VALID_FULL_NAME_PATTERN.matcher(str).matches();
    }

    /**
     * Обработка текста.
     * @param context ядро
     * @param request запрос
     */
    private void textHandler(MathMechBotCore context, Request request) {
        assert request.message().text() != null;
        final String trimmedText = request.message().text().trim();

        if (!validateFullName(trimmedText)) {
            request.bot().sendMessage(Constants.TRY_AGAIN, request.id());
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));

        context.storage.getUserEntries().changeUserEntrySurname(request.id(), strings.get(0));
        context.storage.getUserEntries().changeUserEntryName(request.id(), strings.get(1));
        if (strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM) {
            context.storage.getUserEntries().changeUserEntryPatronym(request.id(), strings.get(2));
        } else {
            context.storage.getUserEntries().changeUserEntryPatronym(request.id(), null);
        }
        context.storage.getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Данные сохранены.").build(), request.id());

        final LocalMessage msg = EditingAdditionalEditState.INSTANCE.enterMessage(context, request);
        request.bot().sendMessage(msg, request.id());
    }
}
