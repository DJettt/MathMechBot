package ru.urfu.mathmechbot;

import java.util.Set;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.fsm.FiniteUserStateMachineImpl;
import ru.urfu.logics.fsm.RequestEvent;
import ru.urfu.mathmechbot.eventhandlers.BackToSpecialtyEventHandler;
import ru.urfu.mathmechbot.eventhandlers.DeleteRequestEventHandler;
import ru.urfu.mathmechbot.eventhandlers.DeletionAcceptEventHandler;
import ru.urfu.mathmechbot.eventhandlers.DeletionInvalidInputEventHandler;
import ru.urfu.mathmechbot.eventhandlers.GenericDeclineEventHandler;
import ru.urfu.mathmechbot.eventhandlers.GenericInvalidInputEventHandler;
import ru.urfu.mathmechbot.eventhandlers.InfoRequestEventHandler;
import ru.urfu.mathmechbot.eventhandlers.SendPredeterminedMessage;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingAcceptEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingSpecialtyChosenEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingValidGroupEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingValidMenEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingValidNameEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingValidSpecialtyEventHandler;
import ru.urfu.mathmechbot.eventhandlers.editing.EditingValidYearEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationAcceptEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationDeclineEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationInvalidConfirmEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationInvalidGroupEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationInvalidSpecialtyEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationInvalidYearEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationValidGroupEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationValidMenEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationValidNameEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationValidSpecialtyEventHandler;
import ru.urfu.mathmechbot.eventhandlers.registration.RegistrationValidYearEventHandler;
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
import ru.urfu.mathmechbot.storages.UserStorage;

/**
 * FuSM, настроенный для работы с MathMechBot.
 */
public final class MMBFiniteUserStateMachine extends FiniteUserStateMachineImpl<MMBCore, MMBUserState> {
    private final UserStorage users;

    /**
     * Конструктор.
     * @param states       набор состояний.
     * @param initialState изначальное состояние.
     * @param users        хранилище пользователей, чтобы обновлять в нём состояние пользователей.
     */
    public MMBFiniteUserStateMachine(Set<MMBUserState> states, MMBUserState initialState, UserStorage users) {
        super(states, initialState);
        this.users = users;
        setupDefaultTransitions();
        setupRegistrationTransitions();
        setupEditingTransitions();
        setupDeletionTransitions();
    }

    @Override
    public void onTransition(@NotNull RequestEvent<MMBCore> event) {
        users.changeUserState(event.request().user().id(), getCurrentState());
    }

    /**
     * Добавляем все переходы, действующие в рамках дефолтного состояния.
     */
    private void setupDefaultTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(HelpEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AlreadyRegisteredEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.ALREADY_REGISTERED))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(NotRegisteredEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.REGISTER_FIRST))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(InfoEvent.class)
                .eventHandler(new InfoRequestEventHandler())
                .build());
    }

    /**
     * Добавляем все переходы, связанные с регистрацией.
     */
    private void setupRegistrationTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.REGISTRATION_NAME)
                .eventType(RegisterEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(ValidInputEvent.class)
                .eventHandler(new RegistrationValidNameEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.REGISTRATION_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.HELP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(ValidInputEvent.class)
                .eventHandler(new RegistrationValidYearEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidYearEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_NAME)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(ValidInputEvent.class)
                .eventHandler(new RegistrationValidSpecialtyEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidSpecialtyEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.YEAR))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(ValidInputEvent.class)
                .eventHandler(new RegistrationValidGroupEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidGroupEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(BackEvent.class)
                .eventHandler(new BackToSpecialtyEventHandler())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(ValidInputEvent.class)
                .eventHandler(new RegistrationValidMenEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.GROUP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new RegistrationAcceptEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new RegistrationDeclineEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidConfirmEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.MEN))
                .build());
    }

    /**
     * Добавляем все переходы, связанные с редактированием.
     */
    private void setupEditingTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(EditEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_FULL_NAME)
                .eventType(FullNameChosenEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.FULL_NAME))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_YEAR)
                .eventType(YearChosenEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_SPECIALITY)
                .eventType(SpecialtyChosenEvent.class)
                .eventHandler(new EditingSpecialtyChosenEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_GROUP)
                .eventType(GroupChosenEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_MEN)
                .eventType(MenChosenEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new EditingValidNameEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_FULL_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new EditingValidYearEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidYearEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new EditingValidSpecialtyEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_SPECIALITY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidSpecialtyEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new EditingValidGroupEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new RegistrationInvalidGroupEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new EditingValidMenEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new EditingAcceptEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(DeclineEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.EDIT_CHOOSE))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new GenericInvalidInputEventHandler())
                .build());
    }

    /**
     * Добавляем все переходы, связанные с удалением.
     */
    private void setupDeletionTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DELETION_CONFIRMATION)
                .eventType(DeleteEvent.class)
                .eventHandler(new DeleteRequestEventHandler())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new DeletionAcceptEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new GenericDeclineEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DELETION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new DeletionInvalidInputEventHandler())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendPredeterminedMessage(Constants.HELP))
                .build());
    }
}
