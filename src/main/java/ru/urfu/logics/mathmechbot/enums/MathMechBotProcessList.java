package ru.urfu.logics.mathmechbot.enums;

import ru.urfu.enums.ProcessList;

/**
 * Список процессов в MathMechBot.
 */
public enum MathMechBotProcessList implements ProcessList {
    DEFAULT(DefaultStateList.class),
    REGISTRATION(RegistrationStateList.class),
    DELETION(DeletionStateList.class);

    private final Class<? extends MathMechBotStateList> processClass;

    /**
     * Устанавливает класс для данного процесса.
     *
     * @param processClass класс процесса.
     */
    MathMechBotProcessList(Class<? extends MathMechBotStateList> processClass) {
        this.processClass = processClass;
    }

    @Override
    public Class<? extends MathMechBotStateList> stateClass() {
        return processClass;
    }
}
