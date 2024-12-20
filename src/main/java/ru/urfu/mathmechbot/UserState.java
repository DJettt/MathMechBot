package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.messagehandlers.DataCheckMessageHandler;
import ru.urfu.mathmechbot.messagehandlers.DefaultStateHandler;
import ru.urfu.mathmechbot.messagehandlers.EditingChooseHandler;
import ru.urfu.mathmechbot.messagehandlers.MessageHandler;
import ru.urfu.mathmechbot.messagehandlers.YesNoBackHandler;
import ru.urfu.mathmechbot.messagehandlers.YesNoHandler;
import ru.urfu.mathmechbot.validators.FullNameValidator;
import ru.urfu.mathmechbot.validators.GroupValidator;
import ru.urfu.mathmechbot.validators.MenValidator;
import ru.urfu.mathmechbot.validators.SpecialtyValidator;
import ru.urfu.mathmechbot.validators.YearValidator;

/**
 * <p>Cостояние пользователя в MathMechBot.</p>
 */
public enum UserState {
    DEFAULT(new DefaultStateHandler()),

    REGISTRATION_NAME(new DataCheckMessageHandler(new FullNameValidator())),
    REGISTRATION_YEAR(new DataCheckMessageHandler(new YearValidator())),
    REGISTRATION_SPECIALTY(new DataCheckMessageHandler(new SpecialtyValidator())),
    REGISTRATION_GROUP(new DataCheckMessageHandler(new GroupValidator())),
    REGISTRATION_MEN(new DataCheckMessageHandler(new MenValidator())),
    REGISTRATION_CONFIRMATION(new YesNoBackHandler()),

    EDITING_CHOOSE(new EditingChooseHandler()),
    EDITING_FULL_NAME(new DataCheckMessageHandler(new FullNameValidator())),
    EDITING_YEAR(new DataCheckMessageHandler(new YearValidator())),
    EDITING_SPECIALITY(new DataCheckMessageHandler(new SpecialtyValidator())),
    EDITING_GROUP(new DataCheckMessageHandler(new GroupValidator())),
    EDITING_MEN(new DataCheckMessageHandler(new MenValidator())),
    EDITING_ADDITIONAL_EDIT(new YesNoHandler()),

    DELETION_CONFIRMATION(new YesNoBackHandler());

    private final MessageHandler messageHandler;

    /**
     * <p>Устанавливает обработчик сообщений, которое будет обрабатывать
     * сообщения от пользователя в данном состоянии.</p>
     *
     * @param messageHandler обработчик сообщений.
     */
    UserState(@NotNull MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * <p>Возвращает обработчик сообщений данного состояния.</p>
     *
     * @return обработчик сообщений.
     */
    @NotNull
    public MessageHandler messageHandler() {
        return messageHandler;
    }
}
