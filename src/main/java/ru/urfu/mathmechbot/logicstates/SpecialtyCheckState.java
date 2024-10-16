package ru.urfu.mathmechbot.logicstates;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.UserEntry;


/**
 * Состояние запроса направления подготовки.
 * Предлагает пользователю направление подготовки
 * из списка, который возвращает метод allowedSpecialties.
 */
public final class SpecialtyCheckState extends DataCheckState {
    private final static Logger LOGGER = LoggerFactory.getLogger(SpecialtyCheckState.class);

    @Override
    protected boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final Optional<UserEntry> userEntryOptional = request
                .context()
                .getStorage()
                .getUserEntries()
                .get(request.user().id());

        if (userEntryOptional.isEmpty()) {
            LOGGER.error("User without entry managed to reach registration_specialty state.");
            throw new RuntimeException();
        } else if (userEntryOptional.get().year() == null) {
            LOGGER.error("User without set year managed to reach registration_specialty state.");
            throw new RuntimeException();
        }

        return getAllowedSpecialties(userEntryOptional.get().year())
                .stream()
                .map(Specialty::getAbbreviation)
                .toList()
                .contains(request.message().text());
    }

    /**
     * Возвращает список разрешённых специальностей.
     * Нужно для наследования от этого класса.
     *
     * @param year год обучения данного студента.
     * @return список разрешённых специальностей.
     */
    @NotNull
    public List<Specialty> getAllowedSpecialties(int year) {
        if (year == 1) {
            return new ArrayList<>(List.of(
                    Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
            ));
        }
        return new ArrayList<>(List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT
        ));
    }
}
