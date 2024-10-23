package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.messagehandlers.DataCheckState;
import ru.urfu.mathmechbot.messagehandlers.DefaultState;
import ru.urfu.mathmechbot.messagehandlers.EditingChooseState;
import ru.urfu.mathmechbot.messagehandlers.MMBCoreState;
import ru.urfu.mathmechbot.messagehandlers.SpecialtyCheckState;
import ru.urfu.mathmechbot.messagehandlers.YesNoBackState;
import ru.urfu.mathmechbot.messagehandlers.YesNoState;
import ru.urfu.mathmechbot.validators.FullNameValidator;
import ru.urfu.mathmechbot.validators.GroupValidator;
import ru.urfu.mathmechbot.validators.MenValidator;
import ru.urfu.mathmechbot.validators.YearValidator;

/**
 * <p>Cостояние пользователя в MathMechBot.</p>
 */
public enum MMBUserState {
    DEFAULT(new DefaultState()),

    REGISTRATION_NAME(new DataCheckState(new FullNameValidator())),
    REGISTRATION_YEAR(new DataCheckState(new YearValidator())),
    REGISTRATION_SPECIALTY(new SpecialtyCheckState()),
    REGISTRATION_GROUP(new DataCheckState(new GroupValidator())),
    REGISTRATION_MEN(new DataCheckState(new MenValidator())),
    REGISTRATION_CONFIRMATION(new YesNoBackState()),

    EDITING_CHOOSE(new EditingChooseState()),
    EDITING_FULL_NAME(new DataCheckState(new FullNameValidator())),
    EDITING_YEAR(new DataCheckState(new YearValidator())),
    EDITING_SPECIALITY(new SpecialtyCheckState()),
    EDITING_GROUP(new DataCheckState(new GroupValidator())),
    EDITING_MEN(new DataCheckState(new MenValidator())),
    EDITING_ADDITIONAL_EDIT(new YesNoState()),

    DELETION_CONFIRMATION(new YesNoBackState());

    private final MMBCoreState stateInstance;

    /**
     * <p>Устанавливает состояние логического ядра, которое должно
     * обрабатывать сообщения от пользователя в данном состоянии.</p>
     *
     * @param stateInstance состояние логического ядра.
     */
    MMBUserState(@NotNull MMBCoreState stateInstance) {
        this.stateInstance = stateInstance;
    }

    /**
     * <p>Возвращает состояние логического ядра, обрабатывающее данное состояние.</p>
     *
     * @return состояние логического ядра.
     */
    @NotNull
    public MMBCoreState logicCoreState() {
        return stateInstance;
    }
}
