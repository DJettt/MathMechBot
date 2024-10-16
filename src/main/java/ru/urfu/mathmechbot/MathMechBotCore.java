package ru.urfu.mathmechbot;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.LogicCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.states.MathMechBotState;
import ru.urfu.mathmechbot.storages.MathMechStorage;


/**
 * Логическое ядро бота, обрабатывающего сообщения Telegram-каналов на предмет упоминания студентов.<br/>
 */
public final class MathMechBotCore implements LogicCore {
    public final MathMechStorage storage;
    private MathMechBotState currentState;

    /**
     * Конструктор.
     *
     * @param storage хранилище данных для логики.
     */
    public MathMechBotCore(@NotNull MathMechStorage storage) {
        this.storage = storage;
        currentState = MathMechBotUserState.DEFAULT.logicCoreState();
    }

    @Override
    public void processMessage(@NotNull Request request) {
        Optional<User> userOptional = storage.users.get(request.id());

        if (userOptional.isEmpty()) {
            storage.users.add(new User(request.id(), MathMechBotUserState.DEFAULT));
            assert storage.users.get(request.id()).isPresent();
        }
        final User user = storage.users.get(request.id()).get();

        currentState = user.currentState().logicCoreState();
        currentState.setContext(this);
        currentState.processMessage(request);
    }
}
