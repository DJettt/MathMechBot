package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.logicstates.DefaultState;
import ru.urfu.mathmechbot.logicstates.EditingChooseState;
import ru.urfu.mathmechbot.logicstates.MMBCoreState;
import ru.urfu.mathmechbot.logicstates.YesNoBackState;
import ru.urfu.mathmechbot.logicstates.YesNoState;
import ru.urfu.mathmechbot.logicstates.checkers.FullNameCheckState;
import ru.urfu.mathmechbot.logicstates.checkers.GroupCheckState;
import ru.urfu.mathmechbot.logicstates.checkers.MenCheckState;
import ru.urfu.mathmechbot.logicstates.checkers.SpecialtyCheckState;
import ru.urfu.mathmechbot.logicstates.checkers.YearCheckState;

/**
 * <p>Cостояние пользователя в MathMechBot.</p>
 */
public enum MMBUserState {
    DEFAULT(new DefaultState()),

    REGISTRATION_NAME(new FullNameCheckState()),
    REGISTRATION_YEAR(new YearCheckState()),
    REGISTRATION_SPECIALTY(new SpecialtyCheckState()),
    REGISTRATION_GROUP(new GroupCheckState()),
    REGISTRATION_MEN(new MenCheckState()),
    REGISTRATION_CONFIRMATION(new YesNoBackState()),

    EDITING_CHOOSE(new EditingChooseState()),
    EDITING_FULL_NAME(new FullNameCheckState()),
    EDITING_YEAR(new YearCheckState()),
    EDITING_SPECIALITY(new SpecialtyCheckState()),
    EDITING_GROUP(new GroupCheckState()),
    EDITING_MEN(new MenCheckState()),
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
