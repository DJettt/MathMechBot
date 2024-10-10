package ru.urfu.logics.mathmechbot.states.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.enums.Specialty;


/**
 * Состояние запроса направления подготовки у первокурсника.
 */
public final class RegistrationFirstYearSpecialtiesState extends RegistrationSpecialitiesState {
    @Override
    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
        ));
    }

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationFirstYearSpecialtiesState(MathMechBotCore context) {
        super(context);
    }
}
