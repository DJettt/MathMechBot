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
        extends FiniteStateMachineImpl<MMBUserState, MMBEvent, EventContext> {
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     */
    public MMBFiniteUserStateMachine(@NotNull UserEntryStorage storage) {
        super(new HashSet<>(List.of(MMBUserState.values())), MMBUserState.DEFAULT);
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
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.HELP)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.ALREADY_REGISTERED)
                .action(new SendConstantMessage(Constants.ALREADY_REGISTERED))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.NOT_REGISTERED)
                .action(new SendConstantMessage(Constants.REGISTER_FIRST))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.INFO)
                .action(new SendUserInfo(userEntryStorage))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с регистрацией.</p>
     */
    private void setupRegistrationTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.REGISTRATION_NAME)
                .event(MMBEvent.REGISTER)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.REGISTRATION_YEAR)
                .event(MMBEvent.VALID_INPUT)
                .action(new CreateUserEntry(userEntryStorage))
                .action(new SaveFullName(userEntryStorage))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.REGISTRATION_NAME)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_NAME)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.BACK)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.HELP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_YEAR)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_YEAR)
                .target(MMBUserState.REGISTRATION_NAME)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_GROUP)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_SPECIALTY)
                .target(MMBUserState.REGISTRATION_YEAR)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_MEN)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_GROUP)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_GROUP)
                .target(MMBUserState.REGISTRATION_SPECIALTY)
                .event(MMBEvent.BACK)
                .action(new AskSpecialty(userEntryStorage))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_CONFIRMATION)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_MEN)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_MEN)
                .target(MMBUserState.REGISTRATION_GROUP)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.DECLINE)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.REGISTRATION_CONFIRMATION)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.REGISTRATION_CONFIRMATION)
                .target(MMBUserState.REGISTRATION_MEN)
                .event(MMBEvent.BACK)
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
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.EDIT)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_FULL_NAME)
                .event(MMBEvent.FULL_NAME_CHOSEN)
                .action(new SendConstantMessage(Constants.FULL_NAME))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_YEAR)
                .event(MMBEvent.YEAR_CHOSEN)
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_SPECIALITY)
                .event(MMBEvent.SPECIALTY_CHOSEN)
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_GROUP)
                .event(MMBEvent.GROUP_CHOSEN)
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_MEN)
                .event(MMBEvent.MEN_CHOSEN)
                .action(new SendConstantMessage(Constants.MEN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_CHOOSE)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveFullName(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_FULL_NAME)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_FULL_NAME)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveYear(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_YEAR)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.YEAR))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_YEAR)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_SPECIALITY)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_SPECIALITY)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_GROUP)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new SendConstantMessage(Constants.GROUP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_GROUP)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.VALID_INPUT)
                .action(new SaveMen(userEntryStorage))
                .action(new EditingInfoSaved())
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_MEN)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_MEN)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.ACCEPT)
                .action(new SendConstantMessage(Constants.SAVED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.EDITING_CHOOSE)
                .event(MMBEvent.DECLINE)
                .action(new SendConstantMessage(Constants.EDIT_CHOOSE))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .target(MMBUserState.EDITING_ADDITIONAL_EDIT)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с удалением.</p>
     */
    private void setupDeletionTransitions() {
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DEFAULT)
                .target(MMBUserState.DELETION_CONFIRMATION)
                .event(MMBEvent.DELETE)
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());

        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.ACCEPT)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(Constants.DELETED))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.DECLINE)
                .action(new SendConstantMessage(Constants.CANCEL))
                .action(new SendConstantMessage(Constants.HELP))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DELETION_CONFIRMATION)
                .event(MMBEvent.INVALID_INPUT)
                .action(new SendConstantMessage(Constants.TRY_AGAIN))
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());
        registerTransition(new MMBTransitionBuilder()
                .source(MMBUserState.DELETION_CONFIRMATION)
                .target(MMBUserState.DEFAULT)
                .event(MMBEvent.BACK)
                .action(new SendConstantMessage(Constants.HELP))
                .build());
    }
}
