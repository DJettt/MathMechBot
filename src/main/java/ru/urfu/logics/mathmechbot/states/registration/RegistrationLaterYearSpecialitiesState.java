package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.mathmechbot.models.Specialty;


/**
 * Состояние запроса направления подготовки для поздних курсов.
 */
public enum RegistrationLaterYearSpecialitiesState implements RegistrationSpecialitiesState {
    INSTANCE;

    @Override
    public List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT
        ));
    }
}
