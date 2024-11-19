package ru.urfu.mathmechbot.validators;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;


/**
 * <p>Проверяет корректность МЕН-группы.</p>
 */
public final class MenValidator implements MessageValidator {
    private final Pattern validMenGroupString = Pattern
            .compile("^\\s*МЕН-\\d{6}\\s*$");

    @Override
    public boolean validateMessageContent(@NotNull MathMechStorageInterface storage,
                                          @NotNull User user,
                                          @NotNull LocalMessage message) {
        if (message.text() == null) {
            return false;
        }
        return validMenGroupString.matcher(message.text()).matches();
    }
}
