package ru.urfu.logics.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Логическое ядро бота, обрабатывающего сообщения Telegram-каналов на предмет упоминания студентов.<br/>
 */
public final class MathMechBotCore implements LogicCore {
    private final MathMechStorage storage;
    private MathMechBotState currentState;

    /**
     * Конструктор.
     *
     * @param storage хранилище данных для логики.
     */
    public MathMechBotCore(@NotNull MathMechStorage storage) {
        this.storage = storage;
        currentState = new DefaultState();
    }

    @Override
    public void processMessage(@NotNull Long chatId, @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = getStorage().getUsers();
        final User user = userStorage
                .get(chatId)
                .orElseGet(() -> {
                    final User newUser = new User(chatId, MathMechBotUserState.DEFAULT);
                    userStorage.add(newUser);
                    return newUser;
                });

        currentState = user.currentState().stateInstance();
        currentState.processMessage(this, chatId, message, bot);
    }

    /**
     * Геттер поля storage.
     * @return storage
     */
    public MathMechStorage getStorage() {
        return storage;
    }
}
