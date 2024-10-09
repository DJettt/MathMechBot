package ru.urfu.logics.mathmechbot;

import ru.urfu.enums.Specialty;

import java.util.ArrayList;
import java.util.List;


/**
 * Состояние запроса направления подготовки у первокурсника.
 */
public class RegistrationFirstYearSpecialtiesState extends RegistrationSpecialitiesState {
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
