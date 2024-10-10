package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.mathmechbot.models.Specialty;


/**
 * Состояние запроса направления подготовки у первокурсника.
 */
public enum RegistrationFirstYearSpecialtiesState implements RegistrationSpecialitiesState {
    INSTANCE;

    @Override
    public List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
        ));
    }
}
