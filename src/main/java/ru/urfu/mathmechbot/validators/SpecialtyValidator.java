package ru.urfu.mathmechbot.validators;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Utils;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;


/**
 * <p>Проверяет корректность направления подготовки.</p>
 */
public final class SpecialtyValidator implements MessageValidator {
    private final Logger logger = LoggerFactory.getLogger(SpecialtyValidator.class);
    private final Utils utils = new Utils();

    @Override
    public boolean validateMessageContent(@NotNull MathMechStorageInterface storage,
                                          @NotNull User user,
                                          @NotNull LocalMessage message) {
        final Optional<UserEntry> userEntryOptional = storage
                .getUserEntries()
                .get(user.id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry managed to reach registration_specialty state.");
            throw new RuntimeException();
        } else if (userEntryOptional.get().year() == null) {
            logger.error("User without set year managed to reach registration_specialty state.");
            throw new RuntimeException();
        }

        return utils.getAllowedSpecialties(userEntryOptional.get().year())
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(message.text());
    }
}
