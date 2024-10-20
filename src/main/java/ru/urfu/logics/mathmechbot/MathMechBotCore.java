package ru.urfu.logics.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.Request;
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
        currentState = DefaultState.INSTANCE;
    }

    @Override
    public void processMessage(@NotNull Request request) {
        final UserStorage userStorage = getStorage().getUsers();
        final User user = userStorage
                .get(request.id())
                .orElseGet(() -> {
                    final User newUser = new User(request.id(), MathMechBotUserState.DEFAULT);
                    userStorage.add(newUser);
                    return newUser;
                });

        currentState = user.currentState().stateInstance();
        currentState.processMessage(this, request);
    }

    /**
     * Геттер поля storage.
     * @return storage
     */
    public MathMechStorage getStorage() {
        return storage;
    }
}
