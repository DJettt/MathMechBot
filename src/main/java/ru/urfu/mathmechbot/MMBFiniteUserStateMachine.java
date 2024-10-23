package ru.urfu.mathmechbot;

import java.util.HashSet;
import java.util.List;
import ru.urfu.fsm.FiniteStateMachineImpl;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.eventhandlers.AskDeletionConfirmation;
import ru.urfu.mathmechbot.eventhandlers.AskRegistrationConfirmation;
import ru.urfu.mathmechbot.eventhandlers.AskSpecialty;
import ru.urfu.mathmechbot.eventhandlers.CreateUserEntry;
import ru.urfu.mathmechbot.eventhandlers.DeleteUserEntry;
import ru.urfu.mathmechbot.eventhandlers.EditingInfoSaved;
import ru.urfu.mathmechbot.eventhandlers.SaveFullName;
import ru.urfu.mathmechbot.eventhandlers.SaveGroup;
import ru.urfu.mathmechbot.eventhandlers.SaveMen;
import ru.urfu.mathmechbot.eventhandlers.SaveSpecialty;
import ru.urfu.mathmechbot.eventhandlers.SaveYear;
import ru.urfu.mathmechbot.eventhandlers.SendConstantMessage;
import ru.urfu.mathmechbot.eventhandlers.SendUserInfo;
import ru.urfu.mathmechbot.events.AcceptEvent;
import ru.urfu.mathmechbot.events.AlreadyRegisteredEvent;
import ru.urfu.mathmechbot.events.BackEvent;
import ru.urfu.mathmechbot.events.DeclineEvent;
import ru.urfu.mathmechbot.events.DeleteEvent;
import ru.urfu.mathmechbot.events.EditEvent;
import ru.urfu.mathmechbot.events.HelpEvent;
import ru.urfu.mathmechbot.events.InfoEvent;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.events.NotRegisteredEvent;
import ru.urfu.mathmechbot.events.RegisterEvent;
import ru.urfu.mathmechbot.events.ValidInputEvent;
import ru.urfu.mathmechbot.events.editing.FullNameChosenEvent;
import ru.urfu.mathmechbot.events.editing.GroupChosenEvent;
import ru.urfu.mathmechbot.events.editing.MenChosenEvent;
import ru.urfu.mathmechbot.events.editing.SpecialtyChosenEvent;
import ru.urfu.mathmechbot.events.editing.YearChosenEvent;

/**
 * <p>FSM, настроенный для работы с MathMechBot.</p>
 */
public final class MMBFiniteUserStateMachine
        extends FiniteStateMachineImpl<RequestEvent<MMBCore>, MMBUserState> {

    /**
     * <p>Конструктор.</p>
     */
    public MMBFiniteUserStateMachine() {
        super(new HashSet<>(List.of(MMBUserState.values())), MMBUserState.DEFAULT);

        setupDefaultTransitions();
        setupRegistrationTransitions();
        setupEditingTransitions();
        setupDeletionTransitions();
    }

    /**
     * <p>Добавляем все переходы, действующие в рамках дефолтного состояния.</p>
     */
    private void setupDefaultTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .eventType(HelpEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .eventType(AlreadyRegisteredEvent.class)
                .eventHandler(new SendConstantMessage(Constants.ALREADY_REGISTERED))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .eventType(NotRegisteredEvent.class)
                .eventHandler(new SendConstantMessage(Constants.REGISTER_FIRST))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .eventType(InfoEvent.class)
                .eventHandler(new SendUserInfo())
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с регистрацией.</p>
     */
    private void setupRegistrationTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.REGISTRATION_NAME)
                .eventType(RegisterEvent.class)
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.REGISTRATION_YEAR)
                .eventType(ValidInputEvent.class)
                .eventHandler(new CreateUserEntry())
                .eventHandler(new SaveFullName())
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.REGISTRATION_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new DeleteUserEntry())
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveYear())
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_NAME)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_GROUP)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveSpecialty())
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_YEAR)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_MEN)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveGroup())
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(BackEvent.class)
                .eventHandler(new AskSpecialty())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveMen())
                .eventHandler(new AskRegistrationConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_GROUP)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new SendConstantMessage(Constants.SAVED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new DeleteUserEntry())
                .eventHandler(new SendConstantMessage(Constants.CANCEL))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new AskRegistrationConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.REGISTRATION_MEN)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с редактированием.</p>
     * TODO: решить, что делать с этими длинными методами.
     */
    @SuppressWarnings("MethodLength")
    private void setupEditingTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(EditEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_FULL_NAME)
                .eventType(FullNameChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_YEAR)
                .eventType(YearChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_SPECIALITY)
                .eventType(SpecialtyChosenEvent.class)
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_GROUP)
                .eventType(GroupChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_MEN)
                .eventType(MenChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveFullName())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_FULL_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveYear())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveSpecialty())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_SPECIALITY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveGroup())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveMen())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new SendConstantMessage(Constants.SAVED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.EDITING_CHOOSE)
                .eventType(DeclineEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с удалением.</p>
     */
    private void setupDeletionTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DELETION_CONFIRMATION)
                .eventType(DeleteEvent.class)
                .eventHandler(new AskDeletionConfirmation())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new DeleteUserEntry())
                .eventHandler(new SendConstantMessage(Constants.DELETED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new SendConstantMessage(Constants.CANCEL))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DELETION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new SendConstantMessage(Constants.TRY_AGAIN))
                .eventHandler(new AskDeletionConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
    }
}
