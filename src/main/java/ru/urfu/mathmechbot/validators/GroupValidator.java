package ru.urfu.mathmechbot.validators;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;


/**
 * <p>Проверяет корректность группы.</p>
 */
public final class GroupValidator implements MessageValidator {
    private final Pattern validGroupStringPattern = Pattern.compile("^[1-5]$");

    @Override
    public boolean validateMessageContent(@NotNull LocalMessage message) {
        if (message.text() == null) {
            return false;
        }
        return validGroupStringPattern.matcher(message.text()).matches();
    }
}
