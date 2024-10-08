package ru.urfu.logics.mathmechbot;

import ru.urfu.enums.Specialty;

import java.util.ArrayList;
import java.util.List;


public class RegistrationLaterYearSpecialitiesState extends RegistrationSpecialitiesState {
    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KN, Specialty.MO, Specialty.MH, Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT
        ));
    }

    public RegistrationLaterYearSpecialitiesState(MathMechBotCore context) {
        super(context);
    }
}
