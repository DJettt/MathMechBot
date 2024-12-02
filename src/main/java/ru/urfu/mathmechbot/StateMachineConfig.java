package ru.urfu.mathmechbot;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.StateMachine;
import ru.urfu.fsm.TransitionBuilder;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
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
import ru.urfu.mathmechbot.actions.SendTimetable;
import ru.urfu.mathmechbot.actions.SendUserInfo;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;
import ru.urfu.mathmechbot.timetable.TimetableFactory;

/**
 * <p>FSM, настроенный для работы с MathMechBot.</p>
 */
public final class StateMachineConfig {
    private final Utils utils = new Utils();

    private final LocalMessage tryAgain = new LocalMessage("Попробуйте снова.");
    private final LocalMessage saved = new LocalMessage("Данные сохранены.");
    private final LocalMessage cancel = new LocalMessage("Отмена...");
    private final LocalMessage help = new LocalMessage("""
            /start - начало общения с ботом
            /help - выводит команды, которые принимает бот
            /register - регистрация
            /info - информация о Вас
            /edit - изменить информацию
            /delete - удалить информацию о Вас
            /timetable - показывает расписание на текущий день""");
    private final LocalMessage fullName = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(utils.makeBackButton()))
            .build();
    private final LocalMessage year = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    utils.makeBackButton()))
            .build();
    private final LocalMessage group = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    utils.makeBackButton()))
            .build();
    private final LocalMessage men = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(List.of(utils.makeBackButton()))
            .build();

    private final StateMachine<UserState, Event, EventContext> fsm;
    private final UserEntryStorage userEntryStorage;
    private final TimetableFactory timetableFactory;


    /**
     * <p>Конструктор.</p>
     *
     * @param fsm     настраиваемый конечный автомат.
     * @param storage хранилище пользовательских записей,
     *                требуется для некоторых действий.
     */
    public StateMachineConfig(@NotNull StateMachine<UserState, Event, EventContext> fsm,
                              @NotNull UserEntryStorage storage,
                              @NotNull TimetableFactory timetableFactory) {
        this.fsm = fsm;
        this.userEntryStorage = storage;
        this.timetableFactory = timetableFactory;
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
        final LocalMessage registerFirst =
                new LocalMessage("Сперва нужно зарегистрироваться.");
        final LocalMessage alreadyRegistered =
                new LocalMessage("Вы уже зарегистрированы. Пока что "
                        + "регистрировать можно только одного человека.");

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.HELP)
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.ALREADY_REGISTERED)
                .action(new SendConstantMessage(alreadyRegistered))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.NOT_REGISTERED)
                .action(new SendConstantMessage(registerFirst))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.INFO)
                .action(new SendUserInfo(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.DEFAULT)
                .event(Event.TIMETABLE)
                .action(new SendTimetable(userEntryStorage, timetableFactory))
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
                .action(new SendConstantMessage(fullName))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.VALID_INPUT)
                .action(new CreateUserEntry(userEntryStorage))
                .action(new SaveFullName(userEntryStorage))
                .action(new SendConstantMessage(year))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_NAME)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(help))
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
                .action(new SendConstantMessage(tryAgain))
                .action(new SendConstantMessage(year))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_YEAR)
                .target(UserState.REGISTRATION_NAME)
                .event(Event.BACK)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(fullName))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.VALID_INPUT)
                .action(new SaveSpecialty(userEntryStorage))
                .action(new SendConstantMessage(group))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_SPECIALTY)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_SPECIALTY)
                .target(UserState.REGISTRATION_YEAR)
                .event(Event.BACK)
                .action(new SendConstantMessage(year))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.VALID_INPUT)
                .action(new SaveGroup(userEntryStorage))
                .action(new SendConstantMessage(men))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_GROUP)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .action(new SendConstantMessage(group))
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
                .action(new SendConstantMessage(tryAgain))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_MEN)
                .target(UserState.REGISTRATION_GROUP)
                .event(Event.BACK)
                .action(new SendConstantMessage(group))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(saved))
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new DeleteUserEntry(userEntryStorage))
                .action(new SendConstantMessage(cancel))
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.REGISTRATION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .action(new AskRegistrationConfirmation(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.REGISTRATION_CONFIRMATION)
                .target(UserState.REGISTRATION_MEN)
                .event(Event.BACK)
                .action(new SendConstantMessage(men))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с редактированием.</p>
     */
    @SuppressWarnings("MethodLength")
    private void setupEditingTransitions() {
        // TODO: решить, что делать с этими длинными декларативными методами.
        final LocalMessage editChoose = new LocalMessageBuilder()
                .text("Что Вы хотите изменить?")
                .buttons(List.of(
                        new LocalButton("ФИО", Constants.EDITING_FULL_NAME),
                        new LocalButton("Курс", Constants.EDITING_YEAR),
                        new LocalButton("Направление", Constants.EDITING_SPECIALITY),
                        new LocalButton("Группа", Constants.EDITING_GROUP),
                        new LocalButton("МЕН", Constants.EDITING_MEN),
                        utils.makeBackButton()))
                .build();

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DEFAULT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.EDIT)
                .action(new SendConstantMessage(editChoose))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_FULL_NAME)
                .event(Event.FULL_NAME_CHOSEN)
                .action(new SendConstantMessage(fullName))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_YEAR)
                .event(Event.YEAR_CHOSEN)
                .action(new SendConstantMessage(year))
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
                .action(new SendConstantMessage(group))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_MEN)
                .event(Event.MEN_CHOSEN)
                .action(new SendConstantMessage(men))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_CHOOSE)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
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
                .action(new SendConstantMessage(tryAgain))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_FULL_NAME)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(editChoose))
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
                .action(new SendConstantMessage(tryAgain))
                .action(new SendConstantMessage(year))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_YEAR)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(editChoose))
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
                .action(new SendConstantMessage(tryAgain))
                .action(new AskSpecialty(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_SPECIALITY)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(editChoose))
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
                .action(new SendConstantMessage(tryAgain))
                .action(new SendConstantMessage(group))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_GROUP)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(editChoose))
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
                .action(new SendConstantMessage(tryAgain))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_MEN)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.BACK)
                .action(new SendConstantMessage(editChoose))
                .build());

        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.DEFAULT)
                .event(Event.ACCEPT)
                .action(new SendConstantMessage(saved))
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.EDITING_CHOOSE)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(editChoose))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.EDITING_ADDITIONAL_EDIT)
                .target(UserState.EDITING_ADDITIONAL_EDIT)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .build());
    }

    /**
     * <p>Добавляем все переходы, связанные с удалением.</p>
     */
    private void setupDeletionTransitions() {
        final LocalMessage deleted = new LocalMessage("Удаляем...");

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
                .action(new SendConstantMessage(deleted))
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.DECLINE)
                .action(new SendConstantMessage(cancel))
                .action(new SendConstantMessage(help))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DELETION_CONFIRMATION)
                .event(Event.INVALID_INPUT)
                .action(new SendConstantMessage(tryAgain))
                .action(new AskDeletionConfirmation(userEntryStorage))
                .build());
        fsm.registerTransition(new MMBTransitionBuilder()
                .source(UserState.DELETION_CONFIRMATION)
                .target(UserState.DEFAULT)
                .event(Event.BACK)
                .action(new SendConstantMessage(help))
                .build());
    }

    /**
     * <p>Билдер объектов Transition с предустановленными типами для MathMechBot.</p>
     */
    private final class MMBTransitionBuilder
            extends TransitionBuilder<UserState, Event, EventContext> {
    }
}
