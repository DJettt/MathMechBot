package ru.urfu.logics.mathmechbot;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.userstates.DefaultUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;


/**
 * Логическое ядро бота, обрабатывающего сообщения Telegram-каналов на предмет упоминания студентов.<br/>
 */
public final class MathMechBotCore implements LogicCore {
    private final static Logger LOGGER = LoggerFactory.getLogger(MathMechBotCore.class);

    public final MathMechStorage storage;
    private MathMechBotState currentState;

    /**
     * Конструктор.
     *
     * @param storage хранилище данных для логики.
     */
    public MathMechBotCore(@NotNull MathMechStorage storage) {
        this.storage = storage;
        currentState = DefaultState.INSTANCE;
    }

    /**
     * Меняет состояние контекста (паттерн "State").
     *
     * @param state новое состояние контекста.
     */
    private void changeState(@NotNull MathMechBotState state) {
        currentState = state;
    }

    @Override
    public void processMessage(@NotNull LocalMessage msg, long chatId, @NotNull Bot bot) {
        User user;
        Optional<User> userOptional = storage.users.get(chatId);

        if (userOptional.isEmpty()) {
            storage.users.add(new User(chatId, DefaultUserState.DEFAULT));
            assert storage.users.get(chatId).isPresent();
        }
        user = storage.users.get(chatId).get();

        LOGGER.info(user.toString());

        changeState(user.currentState().stateInstance());
        currentState.processMessage(this, msg, chatId, bot);
    }
}
