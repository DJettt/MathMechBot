package ru.urfu.mathmechbot.validators;


import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;


/**
 * <p>Проверяет корректность ФИО.</p>
 */
public final class FullNameValidator implements MessageValidator {
    private final Pattern validFullNamePattern =
            Pattern.compile("^\\s*[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?\\s+"
                    + "[А-ЯЁ][а-яё]+(-[А-ЯЁ][а-яё]+)?"
                    + "(\\s+[А-ЯЁ][а-яё]+)?\\s*$");

    @Override
    public boolean validateMessageContent(@NotNull MathMechStorageInterface storage,
                                          @NotNull User user,
                                          @NotNull LocalMessage message) {
        if (message.text() == null) {
            return false;
        }
        return validFullNamePattern.matcher(message.text()).matches();
    }
}
