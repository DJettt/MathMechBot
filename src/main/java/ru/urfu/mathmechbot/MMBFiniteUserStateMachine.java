package ru.urfu.mathmechbot;

import java.util.Set;
import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.FiniteStateMachineImpl;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.eventhandlers.AskDeletionConfirmation;
import ru.urfu.mathmechbot.eventhandlers.AskRegistrationConfirmation;
import ru.urfu.mathmechbot.eventhandlers.AskSpecialty;
import ru.urfu.mathmechbot.eventhandlers.CreateUserEntry;
import ru.urfu.mathmechbot.eventhandlers.DeleteUserEntry;
import ru.urfu.mathmechbot.eventhandlers.EditingInfoSaved;
import ru.urfu.mathmechbot.eventhandlers.InfoRequestEventHandler;
import ru.urfu.mathmechbot.eventhandlers.SaveFullName;
import ru.urfu.mathmechbot.eventhandlers.SaveGroup;
import ru.urfu.mathmechbot.eventhandlers.SaveMen;
import ru.urfu.mathmechbot.eventhandlers.SaveSpecialty;
import ru.urfu.mathmechbot.eventhandlers.SaveYear;
import ru.urfu.mathmechbot.eventhandlers.SendConstantMessage;
import ru.urfu.mathmechbot.eventhandlers.TryAgain;
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
public final class MMBFiniteUserStateMachine
        extends FiniteStateMachineImpl<RequestEvent<MMBCore>, MMBUserState> {
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
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AlreadyRegisteredEvent.class)
                .eventHandler(new SendConstantMessage(Constants.ALREADY_REGISTERED))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(NotRegisteredEvent.class)
                .eventHandler(new SendConstantMessage(Constants.REGISTER_FIRST))
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
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(ValidInputEvent.class)
                .eventHandler(new CreateUserEntry())
                .eventHandler(new SaveFullName())
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.REGISTRATION_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_NAME)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveYear())
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_YEAR)
                .targetState(MMBUserState.REGISTRATION_NAME)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveSpecialty())
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_SPECIALTY)
                .targetState(MMBUserState.REGISTRATION_YEAR)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveGroup())
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_GROUP)
                .targetState(MMBUserState.REGISTRATION_SPECIALTY)
                .eventType(BackEvent.class)
                .eventHandler(new AskSpecialty())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveMen())
                .eventHandler(new AskRegistrationConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_MEN)
                .targetState(MMBUserState.REGISTRATION_GROUP)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new SendConstantMessage(Constants.SAVED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new DeleteUserEntry())
                .eventHandler(new SendConstantMessage(Constants.CANCEL))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.REGISTRATION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new AskRegistrationConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.REGISTRATION_CONFIRMATION)
                .targetState(MMBUserState.REGISTRATION_MEN)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
    }

    /**
     * Добавляем все переходы, связанные с редактированием.
     * TODO: решить, что делать с этими длинными методами.
     */
    @SuppressWarnings("MethodLength")
    private void setupEditingTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DEFAULT)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(EditEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_FULL_NAME)
                .eventType(FullNameChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.FULL_NAME))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_YEAR)
                .eventType(YearChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_SPECIALITY)
                .eventType(SpecialtyChosenEvent.class)
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_GROUP)
                .eventType(GroupChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_MEN)
                .eventType(MenChosenEvent.class)
                .eventHandler(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_CHOOSE)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveFullName())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_FULL_NAME)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_FULL_NAME)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveYear())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_YEAR)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_YEAR)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveSpecialty())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_SPECIALITY)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new AskSpecialty())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_SPECIALITY)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveGroup())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_GROUP)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_GROUP)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(ValidInputEvent.class)
                .eventHandler(new SaveMen())
                .eventHandler(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_MEN)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_MEN)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new SendConstantMessage(Constants.SAVED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.EDITING_CHOOSE)
                .eventType(DeclineEvent.class)
                .eventHandler(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .targetState(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
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
                .eventHandler(new AskDeletionConfirmation())
                .build());

        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(AcceptEvent.class)
                .eventHandler(new DeleteUserEntry())
                .eventHandler(new SendConstantMessage(Constants.DELETED))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(DeclineEvent.class)
                .eventHandler(new SendConstantMessage(Constants.CANCEL))
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DELETION_CONFIRMATION)
                .eventType(InvalidInputEvent.class)
                .eventHandler(new TryAgain())
                .eventHandler(new AskDeletionConfirmation())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .sourceState(MMBUserState.DELETION_CONFIRMATION)
                .targetState(MMBUserState.DEFAULT)
                .eventType(BackEvent.class)
                .eventHandler(new SendConstantMessage(Constants.HELP))
                .build());
    }
}
