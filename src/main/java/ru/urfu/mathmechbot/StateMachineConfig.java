package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.StateMachine;
import ru.urfu.fsm.TransitionBuilder;
import ru.urfu.mathmechbot.actions.AskDeletionConfirmation;
import ru.urfu.mathmechbot.actions.AskRegistrationConfirmation;
import ru.urfu.mathmechbot.actions.AskSpecialty;
import ru.urfu.mathmechbot.actions.CreateUserEntry;
import ru.urfu.mathmechbot.actions.DeleteUserEntry;
import ru.urfu.mathmechbot.actions.EditingInfoSaved;
import ru.urfu.mathmechbot.actions.SaveFullName;
import ru.urfu.mathmechbot.actions.SaveGroup;
import ru.urfu.mathmechbot.actions.SaveMen;
import ru.urfu.mathmechbot.actions.SaveSpecialty;
import ru.urfu.mathmechbot.actions.SaveYear;
import ru.urfu.mathmechbot.actions.SendConstantMessage;
import ru.urfu.mathmechbot.actions.SendUserInfo;
import ru.urfu.mathmechbot.storages.UserEntryStorage;

/**
 * <p>FSM, настроенный для работы с MathMechBot.</p>
 */
public final class StateMachineConfig {
    private final StateMachine<UserState, Event, EventContext> fsm;
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Билдер объектов Transition с предустановленными типами для MathMechBot.</p>
     */
    private final class MMBTransitionBuilder
            extends TransitionBuilder<UserState, Event, EventContext> {
    }


    /**
     * <p>Конструктор.</p>
     *
     * @param fsm настраиваемый конечный автомат. 
     * @param storage хранилище пользовательских записей,
     *                требуется для некоторых действий.
     */
    public StateMachineConfig(@NotNull StateMachine<UserState, Event, EventContext> fsm,
                              @NotNull UserEntryStorage storage) {
        this.fsm = fsm;
        this.userEntryStorage = storage;
    }

    /**
     * <p>Настраивает автомат для работы в MathMechBot.</p>
     *
     * <p>На текущий момент просто создаёт все переходы.</p>
     */
    public void configure() {
        setupDefaultTransitions();
        setupRegistrationTransitions();
        setupEditingTransitions();
        setupDeletionTransitions();
    }

    /**
     * <p>Добавляем все переходы, действующие в рамках дефолтного состояния.</p>
     */
    private void setupDefaultTransitions() {
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.HELP)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.ALREADY_REGISTERED)
                .action(new SendConstantMessage(Constants.ALREADY_REGISTERED))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.NOT_REGISTERED)
                .action(new SendConstantMessage(Constants.REGISTER_FIRST))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.INFO)
                .action(new SendUserInfo(userEntryStorage))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с регистрацией.</p>
     */
    private void setupRegistrationTransitions() {
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.REGISTER)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.VALID_INPUT)
                .action(new CreateUserEntry(userEntryStorage))
                .action(new SaveFullName(userEntryStorage))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.HELP))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.BACK)
                .action(new AskSpecialty(userEntryStorage))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_CONFIRMATION)
                .event(Event.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.REGISTRATION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.MEN))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с редактированием.</p>
     * TODO: решить, что делать с этими длинными методами.
     */
    @SuppressWarnings("MethodLength")
    private void setupEditingTransitions() {
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.EDIT)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_FULL_NAME)
                .event(Event.FULL_NAME_CHOSEN)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_YEAR)
                .event(Event.YEAR_CHOSEN)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_SPECIALITY)
                .event(Event.SPECIALTY_CHOSEN)
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_GROUP)
                .event(Event.GROUP_CHOSEN)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_MEN)
                .event(Event.MEN_CHOSEN)
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveFullName(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_FULL_NAME)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_YEAR)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_SPECIALITY)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_GROUP)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_MEN)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с удалением.</p>
     */
    private void setupDeletionTransitions() {
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DELETION_CONFIRMATION)
                .event(Event.DELETE)
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.DELETED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DELETION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
    }
}
