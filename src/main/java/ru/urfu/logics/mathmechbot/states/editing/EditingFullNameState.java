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
public final class EditingFullNameState implements MathMechBotState {
    private final static int NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM = 3;

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(new ArrayList<>(List.of(new Constants().backButton)))
            .build();
    private final Pattern validFullNamePattern =
            Pattern.compile("^[А-ЯЁ][а-яё]+\\s+[А-ЯЁ][а-яё]+(\\s+[А-ЯЁ][а-яё]+)?$");

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, request);
            case null -> request.bot().sendMessage(new Constants().tryAgain, request.id());
            default -> textHandler(context, request);
        }
    }

    @NotNull
    @Override
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param context ядро
     * @param request запрос
     */
    private void backCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);
        request.bot().sendMessage(new EditingChooseState().enterMessage(context, request), request.id());
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
     * @param context ядро
     * @param request запрос
     */
    private void textHandler(MathMechBotCore context, Request request) {
        assert request.message().text() != null;
        final String trimmedText = request.message().text().trim();

        if (!validateFullName(trimmedText)) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));

        context.getStorage().getUserEntries().changeUserEntrySurname(request.id(), strings.get(0));
        context.getStorage().getUserEntries().changeUserEntryName(request.id(), strings.get(1));
        if (strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM) {
            context.getStorage().getUserEntries().changeUserEntryPatronym(request.id(), strings.get(2));
        } else {
            context.getStorage().getUserEntries().changeUserEntryPatronym(request.id(), null);
        }
        context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_ADDITIONAL_EDIT);
        request.bot().sendMessage(new LocalMessageBuilder().text("Данные сохранены.").build(), request.id());

        final LocalMessage msg = new EditingAdditionalEditState().enterMessage(context, request);
        request.bot().sendMessage(msg, request.id());
    }
}
