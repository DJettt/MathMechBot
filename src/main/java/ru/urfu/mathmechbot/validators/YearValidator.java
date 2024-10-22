package ru.urfu.mathmechbot.validators;


import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;


/**
 * <p>Проверяет корректность года обучения.</p>
 */
public final class YearValidator implements MessageValidator {
    private final Pattern validYearStringPattern = Pattern.compile("^[1-6]$");

    @Override
    public boolean validateMessageContent(@NotNull LocalMessage message) {
        if (message.text() == null) {
            return false;
        }
        return validYearStringPattern.matcher(message.text()).matches();
    }
}
