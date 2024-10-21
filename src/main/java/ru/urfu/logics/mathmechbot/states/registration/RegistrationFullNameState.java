package ru.urfu.logics.mathmechbot.states.registration;


import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.Request;
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

    private final Pattern validFullNamePattern =
            Pattern.compile("^[А-ЯЁ][а-яё]+\\s+[А-ЯЁ][а-яё]+(\\s+[А-ЯЁ][а-яё]+)?$");

    /**
     * Проверяет корректность введенного ФИО.
     *
     * @param str ФИО (или ФИ).
     * @return корректна ли строка.
     */
    public boolean validateFullName(String str) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        return validFullNamePattern.matcher(str).matches();
    }

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        switch (request.message().text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, request);
            case null -> request.bot().sendMessage(new Constants().tryAgain, request.id());
            default -> textHandler(contextCore, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        return new LocalMessage("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""");
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть в основное состояние.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(request.id());
        userEntryOptional.ifPresent(contextCore.getStorage().getUserEntries()::delete);
        contextCore.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DEFAULT);
        request.bot().sendMessage(new DefaultState().enterMessage(contextCore, request), request.id());
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является ФИО или ФИ (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос года обучения.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param request запрос.
     */
    public void textHandler(@NotNull MathMechBotCore contextCore, @NotNull Request request) {
        assert request.message().text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        final String trimmedText = request.message().text().trim();

        if (!validateFullName(trimmedText)) {
            request.bot().sendMessage(new Constants().tryAgain, request.id());
            return;
        }

        final List<String> strings = List.of(trimmedText.split("\\s+"));
        final boolean hasPatronym = strings.size() == NUMBER_OF_WORDS_IN_FULL_NAME_WITH_PATRONYM;

        userEntryStorage.add(new UserEntry(
                request.id(), strings.get(0), strings.get(1), (hasPatronym) ? strings.get(2) : null,
                null, null, null, null, request.id()));
        userStorage.changeUserState(request.id(), MathMechBotUserState.REGISTRATION_YEAR);

        final LocalMessage msg = new RegistrationYearState().enterMessage(contextCore, request);
        request.bot().sendMessage(msg, request.id());
    }
}
