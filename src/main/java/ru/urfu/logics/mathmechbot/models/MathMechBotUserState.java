package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.states.deletion.DeletionConfirmationState;
import ru.urfu.logics.mathmechbot.states.editing.EditingAdditionalEditState;
import ru.urfu.logics.mathmechbot.states.editing.EditingChooseState;
import ru.urfu.logics.mathmechbot.states.editing.EditingFullNameState;
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

    REGISTRATION_NAME(RegistrationFullNameState.INSTANCE),
    REGISTRATION_YEAR(RegistrationYearState.INSTANCE),
    REGISTRATION_SPECIALTY(RegistrationSpecialtyState.INSTANCE),
    REGISTRATION_GROUP(RegistrationGroupState.INSTANCE),
    REGISTRATION_MEN(RegistrationMenGroupState.INSTANCE),
    REGISTRATION_CONFIRMATION(RegistrationConfirmationState.INSTANCE),

    EDITING_CHOOSE(EditingChooseState.INSTANCE),
    EDITING_FULL_NAME(EditingFullNameState.INSTANCE),
    EDITING_ADDITIONAL_EDIT(EditingAdditionalEditState.INSTANCE),
    EDITING_YEAR(EditingYearState.INSTANCE),

    DELETION_CONFIRMATION(DeletionConfirmationState.INSTANCE);


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
