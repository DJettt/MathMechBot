package ru.urfu.mathmechbot;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.fsm.FiniteStateMachine;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.BotProcessMessageRequest;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;


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
        this.fsm = new MMBFiniteUserStateMachine(
                new HashSet<>(List.of(MMBUserState.values())),
                MMBUserState.DEFAULT,
                storage.getUsers());
    }

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        Optional<User> userOptional = getStorage().getUsers().get(request.id());

        if (userOptional.isEmpty()) {
            getStorage().getUsers().add(new User(request.id(), MMBUserState.DEFAULT));
            assert getStorage().getUsers().get(request.id()).isPresent();
        }
        final User user = getStorage().getUsers().get(request.id()).get();
        final MMBUserState currentState = user.currentState();
        fsm.setCurrentState(currentState);

        currentState.logicCoreState().processMessage(new ContextProcessMessageRequest<>(
                this, user, request.message(), request.bot()));
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
