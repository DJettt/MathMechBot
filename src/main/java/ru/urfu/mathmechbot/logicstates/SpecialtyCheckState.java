package ru.urfu.mathmechbot.logicstates;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.validators.MenValidator;


/**
 * <p>Проверяет корректность направления подготовки.</p>
 */
public final class SpecialtyCheckState extends DataCheckState {
    private final Logger logger = LoggerFactory.getLogger(SpecialtyCheckState.class);

    /**
     * <p>Конструктор. TODO: надо переделать в валилатор.</p>
     */
    public SpecialtyCheckState() {
        super(new MenValidator());
    }

    @Override
    public boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final Optional<UserEntry> userEntryOptional = request
                .context()
                .getStorage()
                .getUserEntries()
                .get(request.user().id());

        if (userEntryOptional.isEmpty()) {
            logger.error("User without entry managed to reach registration_specialty state.");
            throw new RuntimeException();
        } else if (userEntryOptional.get().year() == null) {
            logger.error("User without set year managed to reach registration_specialty state.");
            throw new RuntimeException();
        }

        return getAllowedSpecialties(userEntryOptional.get().year())
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(request.message().text());
    }

    /**
     * <p>Возвращает список разрешённых
     * специальностей для переданного года обучения.</p>
     *
     * @param year год обучения данного студента.
     * @return список разрешённых специальностей.
     */
    @NotNull
    public List<Specialty> getAllowedSpecialties(int year) {
        if (year == 1) {
            return List.of(Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT);
        }
        return List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT,
                Specialty.PM, Specialty.KB, Specialty.FT);
    }
}
