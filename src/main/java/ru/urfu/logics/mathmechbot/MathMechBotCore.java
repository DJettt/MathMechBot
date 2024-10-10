package ru.urfu.logics.mathmechbot;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;
import ru.urfu.logics.mathmechbot.userstates.DefaultUserState;


/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public final class MathMechBotCore implements LogicCore {
    private final static Logger LOGGER = LoggerFactory.getLogger(MathMechBotCore.class);

    private MathMechBotState currentState;
    public final MathMechStorage storage;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        currentState = DefaultState.INSTANCE;
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
    }

    /**
     * Меняет состояние контекста (см. паттерн "State").
     * Состояния могут вызывать этот метод.
     *
     * @param state новое состояние контекста.
     */
    private void changeState(@NotNull MathMechBotState state) {
        currentState = state;
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        User user = storage.users.getById(chatId);
        if (user == null) {
            storage.users.add(new User(chatId, DefaultUserState.DEFAULT));
            user = storage.users.getById(chatId);
        }
        LOGGER.info(user.toString());

        changeState(user.currentState().stateInstance());
        currentState.processMessage(this, msg, chatId, bot);
    }
}
