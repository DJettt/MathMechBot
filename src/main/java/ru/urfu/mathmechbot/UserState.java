package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.messagehandlers.DataCheckMessageHandler;
import ru.urfu.mathmechbot.messagehandlers.DefaultStateHandler;
import ru.urfu.mathmechbot.messagehandlers.EditingChooseHandler;
import ru.urfu.mathmechbot.messagehandlers.MessageHandler;
import ru.urfu.mathmechbot.messagehandlers.SpecialtyHandler;
import ru.urfu.mathmechbot.messagehandlers.YesNoBackHandler;
import ru.urfu.mathmechbot.messagehandlers.YesNoHandler;
import ru.urfu.mathmechbot.validators.FullNameValidator;
import ru.urfu.mathmechbot.validators.GroupValidator;
import ru.urfu.mathmechbot.validators.MenValidator;
import ru.urfu.mathmechbot.validators.YearValidator;

/**
 * <p>Cостояние пользователя в MathMechBot.</p>
 */
public enum UserState {
    DEFAULT(new DefaultStateHandler()),

    REGISTRATION_NAME(new DataCheckMessageHandler(new FullNameValidator())),
    REGISTRATION_YEAR(new DataCheckMessageHandler(new YearValidator())),
    REGISTRATION_SPECIALTY(new SpecialtyHandler()),
    REGISTRATION_GROUP(new DataCheckMessageHandler(new GroupValidator())),
    REGISTRATION_MEN(new DataCheckMessageHandler(new MenValidator())),
    REGISTRATION_CONFIRMATION(new YesNoBackHandler()),

    EDITING_CHOOSE(new EditingChooseHandler()),
    EDITING_FULL_NAME(new DataCheckMessageHandler(new FullNameValidator())),
    EDITING_YEAR(new DataCheckMessageHandler(new YearValidator())),
    EDITING_SPECIALITY(new SpecialtyHandler()),
    EDITING_GROUP(new DataCheckMessageHandler(new GroupValidator())),
    EDITING_MEN(new DataCheckMessageHandler(new MenValidator())),
    EDITING_ADDITIONAL_EDIT(new YesNoHandler()),

    DELETION_CONFIRMATION(new YesNoBackHandler());

    private final MessageHandler stateInstance;

    /**
     * <p>Устанавливает состояние логического ядра, которое должно
     * обрабатывать сообщения от пользователя в данном состоянии.</p>
     *
     * @param stateInstance состояние логического ядра.
     */
    UserState(@NotNull MessageHandler stateInstance) {
        this.stateInstance = stateInstance;
    }

    /**
     * <p>Возвращает состояние логического ядра, обрабатывающее данное состояние.</p>
     *
     * @return состояние логического ядра.
     */
    @NotNull
    public MessageHandler logicCoreState() {
        return stateInstance;
    }
}
