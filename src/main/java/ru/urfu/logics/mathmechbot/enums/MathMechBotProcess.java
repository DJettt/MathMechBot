package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.enums.DefaultState;
import ru.urfu.enums.DeletionState;
import ru.urfu.enums.Process;
import ru.urfu.enums.RegistrationState;
import ru.urfu.enums.State;

public enum MathMechBotProcess implements Process {
    DEFAULT(DefaultState.class),
    REGISTRATION(RegistrationState.class),
    DELETION(DeletionState.class);

    private final Class<? extends State> processClass;

    MathMechBotProcess(Class<? extends State> processClass) {
        this.processClass = processClass;
    }

    @Override
    public Class<? extends State> stateClass() {
        return processClass;
    }
}
