package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.states.deletion.DeletionConfirmationState;
import ru.urfu.logics.mathmechbot.states.editing.EditingAdditionalEditState;
import ru.urfu.logics.mathmechbot.states.editing.EditingChooseState;
import ru.urfu.logics.mathmechbot.states.editing.EditingFullNameState;
import ru.urfu.logics.mathmechbot.states.editing.EditingGroupState;
import ru.urfu.logics.mathmechbot.states.editing.EditingMenState;
import ru.urfu.logics.mathmechbot.states.editing.EditingSpecialityState;
import ru.urfu.logics.mathmechbot.states.editing.EditingYearState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationConfirmationState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationFullNameState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationGroupState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationMenGroupState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationSpecialtyState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationYearState;

/**
 * Cостояние пользователя в MathMechBot.
 */
public enum MathMechBotUserState implements UserState {
    DEFAULT(DefaultState.INSTANCE),

    REGISTRATION_NAME(new RegistrationFullNameState()),
    REGISTRATION_YEAR(new RegistrationYearState()),
    REGISTRATION_SPECIALTY(new RegistrationSpecialtyState()),
    REGISTRATION_GROUP(new RegistrationGroupState()),
    REGISTRATION_MEN(new RegistrationMenGroupState()),
    REGISTRATION_CONFIRMATION(new RegistrationConfirmationState()),

    EDITING_CHOOSE(new EditingChooseState()),
    EDITING_FULL_NAME(new EditingFullNameState()),
    EDITING_ADDITIONAL_EDIT(new EditingAdditionalEditState()),
    EDITING_YEAR(new EditingYearState()),
    EDITING_SPECIALITY(new EditingSpecialityState()),
    EDITING_GROUP(new EditingGroupState()),
    EDITING_MEN(new EditingMenState()),

    DELETION_CONFIRMATION(new DeletionConfirmationState());


    private final MathMechBotState stateInstance;

    /**
     * Устанавливает инстанцию данного состояния.
     *
     * @param stateInstance инстанция состояния.
     */
    MathMechBotUserState(@NotNull MathMechBotState stateInstance) {
        this.stateInstance = stateInstance;
    }

    @Override
    @NotNull
    public MathMechBotState stateInstance() {
        return stateInstance;
    }
}
