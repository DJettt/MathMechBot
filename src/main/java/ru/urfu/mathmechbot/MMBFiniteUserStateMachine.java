package ru.urfu.mathmechbot;

import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.FiniteStateMachineImpl;
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
public final class MMBFiniteUserStateMachine
        extends FiniteStateMachineImpl<UserState, Event, EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage хранилище пользовательских записей,
     *                требуется для некоторых действий.
     */
    public MMBFiniteUserStateMachine(@NotNull UserEntryStorage storage) {
        super(new HashSet<>(List.of(UserState.values())), UserState.DEFAULT);
        this.userEntryStorage = storage;

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
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.HELP)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.ALREADY_REGISTERED)
                .action(new SendConstantMessage(Constants.ALREADY_REGISTERED))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.NOT_REGISTERED)
                .action(new SendConstantMessage(Constants.REGISTER_FIRST))
                .build());
        registerTransition(new MMBTransitionBuilder()
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
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.REGISTER)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.VALID_INPUT)
                .action(new CreateUserEntry(userEntryStorage))
                .action(new SaveFullName(userEntryStorage))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.HELP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.BACK)
                .action(new AskSpecialty(userEntryStorage))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_CONFIRMATION)
                .event(Event.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.REGISTRATION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
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
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.EDIT)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_FULL_NAME)
                .event(Event.FULL_NAME_CHOSEN)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_YEAR)
                .event(Event.YEAR_CHOSEN)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_SPECIALITY)
                .event(Event.SPECIALTY_CHOSEN)
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_GROUP)
                .event(Event.GROUP_CHOSEN)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_MEN)
                .event(Event.MEN_CHOSEN)
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveFullName(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_FULL_NAME)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_YEAR)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_SPECIALITY)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_GROUP)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_MEN)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());
        registerTransition(new MMBTransitionBuilder()
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
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DELETION_CONFIRMATION)
                .event(Event.DELETE)
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.DELETED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DELETION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
    }
}
