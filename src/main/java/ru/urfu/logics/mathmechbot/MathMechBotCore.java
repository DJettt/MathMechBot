package ru.urfu.logics.mathmechbot;

import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.State;
import ru.urfu.logics.mathmechbot.enums.DefaultStateList;
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
    private void changeState(State state) {
        currentState = state;
    }

    /**
     * Определяет состояние контекста для данного пользователя.
     *
     * @param user пользователь.
     * @return состояние, в котором должен пребывать данный пользователь.
     */
    private @Nullable MathMechBotState getStateBasedOnUser(@NotNull User user) {
        try {
            return user.currentState().stateClass().getConstructor(MathMechBotCore.class).newInstance(this);
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
        User user = users.getById(chatId);
        if (user == null) {
            users.add(new User(chatId, DefaultStateList.DEFAULT));
            user = users.getById(chatId);
        }
        LOGGER.info(user.toString());

        final MathMechBotState newState = getStateBasedOnUser(user);

        if (newState == null) {
            LOGGER.error("Couldn't determine state for user.");
            return;
        }

        changeState(newState);
        currentState.processMessage(msg, chatId, bot);
    }
}
