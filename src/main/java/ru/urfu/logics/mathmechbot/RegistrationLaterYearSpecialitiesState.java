package ru.urfu.logics.mathmechbot;

import ru.urfu.enums.Specialty;

import java.util.ArrayList;
import java.util.List;


/**
 * Состояние запроса направления подготовки для поздних курсов.
 */
public class RegistrationLaterYearSpecialitiesState extends RegistrationSpecialitiesState {
    @Override
    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT
        ));
    }

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationLaterYearSpecialitiesState(MathMechBotCore context) {
        super(context);
    }
}
