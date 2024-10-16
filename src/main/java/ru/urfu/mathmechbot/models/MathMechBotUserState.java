package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.UserState;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.states.DefaultState;
import ru.urfu.mathmechbot.states.MathMechBotState;
import ru.urfu.mathmechbot.states.deletion.DeletionConfirmationState;
import ru.urfu.mathmechbot.states.registration.RegistrationConfirmationState;
import ru.urfu.mathmechbot.states.registration.RegistrationFullNameState;
import ru.urfu.mathmechbot.states.registration.RegistrationGroupState;
import ru.urfu.mathmechbot.states.registration.RegistrationMenGroupState;
import ru.urfu.mathmechbot.states.registration.RegistrationSpecialtyState;
import ru.urfu.mathmechbot.states.registration.RegistrationYearState;

/**
 * Cостояние пользователя в MathMechBot.
 */
public enum MathMechBotUserState implements UserState<MathMechBotCore, MathMechBotState> {
    DEFAULT(new DefaultState()),

    REGISTRATION_NAME(new RegistrationFullNameState()),
    REGISTRATION_YEAR(new RegistrationYearState()),
    REGISTRATION_SPECIALTY(new RegistrationSpecialtyState()),
    REGISTRATION_GROUP(new RegistrationGroupState()),
    REGISTRATION_MEN(new RegistrationMenGroupState()),
    REGISTRATION_CONFIRMATION(new RegistrationConfirmationState()),

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
    public MathMechBotState logicCoreState() {
        return stateInstance;
    }
}
