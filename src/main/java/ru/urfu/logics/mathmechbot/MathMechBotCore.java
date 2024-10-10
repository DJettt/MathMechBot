package ru.urfu.logics.mathmechbot;

import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.LogicCoreState;
import ru.urfu.logics.mathmechbot.enums.DefaultUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;


/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public final class MathMechBotCore implements LogicCore {
    private final static Logger LOGGER = LoggerFactory.getLogger(MathMechBotCore.class);

    private LogicCoreState currentState;
    public final MathMechStorage storage;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        currentState = new ru.urfu.logics.mathmechbot.states.DefaultState(this);
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
    }

    /**
     * Меняет состояние контекста (см. паттерн "State").
     * Состояния могут вызывать этот метод.
     *
     * @param state новое состояние контекста.
     */
    private void changeState(LogicCoreState state) {
        currentState = state;
    }

    /**
     * Определяет состояние контекста для данного пользователя.
     *
     * @param user пользователь.
     * @return состояние, в котором должен пребывать данный пользователь.
     */
    private @Nullable MathMechBotState getUserState(@NotNull User user) {
        try {
            return user.currentState().userStateClass().getConstructor(MathMechBotCore.class).newInstance(this);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Constructor of the state {} was not found", user.currentState(), e);
            return null;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("Couldn't create instance of {} state", user.currentState(), e);
            return null;
        }
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        User user = storage.users.getById(chatId);
        if (user == null) {
            storage.users.add(new User(chatId, DefaultUserState.DEFAULT));
            user = storage.users.getById(chatId);
        }
        LOGGER.info(user.toString());

        final MathMechBotState newState = getUserState(user);
        if (newState == null) {
            LOGGER.error("Couldn't determine state for user, setting default.");
            storage.users.changeUserState(chatId, DefaultUserState.DEFAULT);
        }

        changeState(newState);
        currentState.processMessage(msg, chatId, bot);
    }
}
