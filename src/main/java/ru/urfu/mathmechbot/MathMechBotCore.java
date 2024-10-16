package ru.urfu.mathmechbot;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.BotProcessMessageRequest;
import ru.urfu.logics.LogicCore;
import ru.urfu.mathmechbot.fsm.FiniteUserStateMachine;
import ru.urfu.mathmechbot.fsm.FiniteUserStateMachineImpl;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.states.MathMechBotState;
import ru.urfu.mathmechbot.storages.MathMechStorage;


/**
 * Логическое ядро бота, обрабатывающего сообщения Telegram-каналов на предмет упоминания студентов.<br/>
 */
public final class MathMechBotCore implements LogicCore {
    private final MathMechStorage storage;
    private final FiniteUserStateMachine fsm;

    /**
     * Конструктор.
     *
     * @param storage хранилище данных для логики.
     */
    public MathMechBotCore(@NotNull MathMechStorage storage) {
        this.storage = storage;
        this.fsm = new FiniteUserStateMachineImpl(
                new HashSet<>(List.of(MathMechBotUserState.values())),
                MathMechBotUserState.DEFAULT,
                storage.getUsers());
    }

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        Optional<User> userOptional = getStorage().getUsers().get(request.id());

        if (userOptional.isEmpty()) {
            getStorage().getUsers().add(new User(request.id(), MathMechBotUserState.DEFAULT));
            assert getStorage().getUsers().get(request.id()).isPresent();
        }
        final User user = getStorage().getUsers().get(request.id()).get();

        final MathMechBotState currentState = user.currentState().logicCoreState();
        currentState.setContext(this);
        currentState.processMessage(request);
    }

    /**
     * Геттер поля storage.
     * @return storage
     */
    public MathMechStorage getStorage() {
        return storage;
    }
}
