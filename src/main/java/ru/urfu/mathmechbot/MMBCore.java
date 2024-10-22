package ru.urfu.mathmechbot;

import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.fsm.FiniteStateMachine;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserStorage;


/**
 * <p>Логическое ядро бота, обрабатывающего сообщения
 * Telegram-каналов на предмет упоминания студентов.<p/>
 */
public final class MMBCore implements LogicCore {
    private final MathMechStorage storage;
    private final FiniteStateMachine<RequestEvent<MMBCore>, MMBUserState> fsm;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage хранилище данных для логики.
     */
    public MMBCore(@NotNull MathMechStorage storage) {
        this.storage = storage;
        this.fsm = new MMBFiniteUserStateMachine(storage.getUsers());
    }

    @Override
    public void processMessage(@NotNull Long id, @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = storage.getUsers();
        final User user = userStorage
                .get(id)
                .orElseGet(() -> {
                    final User newUser = new User(id, MMBUserState.DEFAULT);
                    userStorage.add(newUser);
                    return newUser;
                });

        final MMBUserState currentState = user.currentState();
        fsm.setCurrentState(currentState);

        currentState.logicCoreState()
                .processMessage(new ContextProcessMessageRequest<>(
                        this, user, message, bot));
    }

    /**
     * <p>Геттер поля storage.</p>
     *
     * @return storage
     */
    public MathMechStorage getStorage() {
        return storage;
    }

    /**
     * <p>Геттер поля fsm.</p>
     *
     * @return fsm (конечный автомат).
     */
    public FiniteStateMachine<RequestEvent<MMBCore>, MMBUserState> getFsm() {
        return fsm;
    }
}
