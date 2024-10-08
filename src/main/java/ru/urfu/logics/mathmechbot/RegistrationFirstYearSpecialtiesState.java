package ru.urfu.logics.mathmechbot;

import ru.urfu.enums.Specialty;

import java.util.ArrayList;
import java.util.List;


public class RegistrationFirstYearSpecialtiesState extends RegistrationSpecialitiesState {
    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>(List.of(
                Specialty.KNMO, Specialty.MMP, Specialty.KB, Specialty.FT
        ));
    }

    public RegistrationFirstYearSpecialtiesState(MathMechBotCore context) {
        super(context);
    }
}
