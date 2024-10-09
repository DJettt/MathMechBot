package ru.urfu.logics.mathmechbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.enums.DeletionState;
import ru.urfu.enums.RegistrationState;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.State;
import ru.urfu.logics.mathmechbot.enums.MathMechBotProcess;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public class MathMechBotCore implements LogicCore {
    private final static Logger LOGGER = LoggerFactory.getLogger(MathMechBotCore.class);
    private State currentState;

    final UserStorage users;
    final UserEntryStorage userEntries;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        currentState = new DefaultState(this);
        users = new UserArrayStorage();
        userEntries = new UserEntryArrayStorage();
    }

    /**
     * Меняет состояние контекста (см. паттерн "State").
     * Состояния могут вызывать этот метод.
     *
     * @param state новое состояние контекста.
     */
    void changeState(State state) {
        currentState = state;
    }

    /**
     * Определяет состояние контекста для данного пользователя.
     *
     * @param user пользователь.
     * @return состояние, в котором должен пребывать данный пользователь.
     */
    private @Nullable State getStateBasedOnUser(@NotNull User user) {
        final State state = switch (user.currentProcess()) {
            case null -> new DefaultState(this);
            case MathMechBotProcess.DEFAULT -> new DefaultState(this);

            case MathMechBotProcess.REGISTRATION -> switch (user.currentState()) {
                case RegistrationState.NAME -> new RegistrationFullNameState(this);
                case RegistrationState.YEAR -> new RegistrationYearState(this);
                case RegistrationState.SPECIALTY1 -> new RegistrationFirstYearSpecialtiesState(this);
                case RegistrationState.SPECIALTY2 -> new RegistrationLaterYearSpecialitiesState(this);
                case RegistrationState.GROUP -> new RegistrationGroupState(this);
                case RegistrationState.MEN -> new RegistrationMenGroupState(this);
                case RegistrationState.CONFIRMATION -> new RegistrationConfirmationState(this);
                case null, default -> null;
            };
            case MathMechBotProcess.DELETION -> switch (user.currentState()) {
                case DeletionState.CONFIRMATION -> new DeletionConfirmationState(this);
                case null, default -> null;
            };
        };

        if (state == null) {
            LOGGER.error("Unknown user state: {}", user.currentState());
            return null;
        }

        return state;
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        final User user = users.getById(chatId);
        State newState;

        if (user == null) {
            newState = new DefaultState(this);
        } else {
            newState = getStateBasedOnUser(user);
        }

        if (newState == null) {
            LOGGER.error("Couldn't determine state for user.");
            return;
        }

        changeState(newState);
        currentState.processMessage(msg, chatId, bot);
    }
}
